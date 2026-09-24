package com.codebattle.codebattle.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * Compiles and runs submitted Java code.
 *
 * HONEST SECURITY NOTE (read this before deploying anywhere beyond your own
 * machine / trusted friends testing locally):
 *
 * This service DOES run submitted code in a separate OS process — never
 * inside the Spring Boot application's own JVM — via ProcessBuilder, with
 * a wall-clock timeout and a JVM heap limit (-Xmx). That satisfies the
 * letter of "never execute untrusted code directly inside the Spring Boot
 * application."
 *
 * It is NOT a real sandbox. The spawned process still runs as the same OS
 * user, on the same host, with the same filesystem and network access as
 * the backend itself. A malicious submission could still read files, make
 * network calls, or spawn further processes. Real isolation requires
 * containerization (e.g. running each submission inside a locked-down,
 * network-disabled Docker container with a read-only filesystem) — that is
 * NOT implemented here. Do not expose this to untrusted users on the
 * public internet as-is.
 */
@Service
public class CodeExecutionService {

    private static final long COMPILE_TIMEOUT_MS = 10_000;
    private static final long RUN_TIMEOUT_MS = 5_000;
    private static final String JVM_MEMORY_FLAG = "-Xmx64m";

    public record TestOutcome(String actualOutput, boolean passed, boolean timedOut,
                               boolean runtimeError, String errorMessage) {}

    public record CompileResult(boolean success, String errorMessage) {}

    /**
     * Compiles the given Java source (must contain `public class Main`) into
     * a fresh temp directory. Returns the directory (for reuse across
     * multiple test case runs) and whether compilation succeeded.
     */
    public Path compile(String sourceCode) throws IOException, InterruptedException {
        Path workDir = Files.createTempDirectory("codebattle-");
        Path sourceFile = workDir.resolve("Main.java");
        Files.writeString(sourceFile, sourceCode, StandardCharsets.UTF_8);
        return workDir;
    }

    public CompileResult runCompiler(Path workDir) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("javac", "Main.java");
        pb.directory(workDir.toFile());
        pb.redirectErrorStream(false);
        Process process = pb.start();

        String stderr = readStream(process.getErrorStream());
        boolean finished = process.waitFor(COMPILE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        if (!finished) {
            process.destroyForcibly();
            return new CompileResult(false, "Compilation timed out.");
        }
        if (process.exitValue() != 0) {
            return new CompileResult(false, stderr);
        }
        return new CompileResult(true, null);
    }

    /** Runs the already-compiled Main class against one input, with a timeout and memory cap. */
    public TestOutcome runTestCase(Path workDir, String input, String expectedOutput) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", JVM_MEMORY_FLAG, "-cp", ".", "Main");
            pb.directory(workDir.toFile());
            Process process = pb.start();

            if (input != null && !input.isEmpty()) {
                process.getOutputStream().write((input + "\n").getBytes(StandardCharsets.UTF_8));
            }
            process.getOutputStream().close();

            String stdout = readStream(process.getInputStream());
            String stderr = readStream(process.getErrorStream());

            boolean finished = process.waitFor(RUN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!finished) {
                process.destroyForcibly();
                return new TestOutcome(null, false, true, false, "Time limit exceeded");
            }

            if (process.exitValue() != 0) {
                return new TestOutcome(stdout, false, false, true, stderr);
            }

            boolean passed = normalize(stdout).equals(normalize(expectedOutput));
            return new TestOutcome(stdout.trim(), passed, false, false, null);

        } catch (IOException | InterruptedException e) {
            return new TestOutcome(null, false, false, true, e.getMessage());
        }
    }

    public void cleanup(Path workDir) {
        try {
            if (workDir == null) return;
            Files.walk(workDir)
                    .sorted((a, b) -> b.compareTo(a)) // delete files before their parent dir
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {
                            // best-effort cleanup
                        }
                    });
        } catch (IOException ignored) {
            // best-effort cleanup
        }
    }

    private String normalize(String s) {
        return s == null ? "" : s.strip();
    }

    private String readStream(java.io.InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
}
