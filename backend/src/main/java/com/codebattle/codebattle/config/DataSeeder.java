package com.codebattle.codebattle.config;

import com.codebattle.codebattle.entity.Question;
import com.codebattle.codebattle.entity.Room;
import com.codebattle.codebattle.entity.TestCase;
import com.codebattle.codebattle.repository.QuestionRepository;
import com.codebattle.codebattle.repository.TestCaseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds 150 sample DSA questions (30 per topic) so the app is playable
 * out of the box. Every starter code stub follows the same convention:
 * read input from stdin, print the answer to stdout, exact string match
 * (after trimming whitespace) is used for grading.
 *
 * All expected outputs in this file were computed programmatically from a
 * reference implementation of each problem (not hand-typed), and several
 * problems that can have multiple valid correct answers (e.g. "longest
 * palindromic substring", "level with the maximum sum") had their test
 * inputs specifically checked to have a single unique correct answer, so
 * exact-string-match grading is safe to use for them.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final QuestionRepository questionRepository;
    private final TestCaseRepository testCaseRepository;

    public DataSeeder(QuestionRepository questionRepository, TestCaseRepository testCaseRepository) {
        this.questionRepository = questionRepository;
        this.testCaseRepository = testCaseRepository;
    }

    @Override
    public void run(String... args) {
        if (questionRepository.count() > 0) {
            return; // already seeded
        }

        seedArrays();
        seedStrings();
        seedLinkedList();
        seedTrees();
        seedGraphs();
    }

    private Question question(String title, String description, Room.Topic topic,
                               Question.Difficulty difficulty, String starterCode,
                               String examples, String constraints) {
        Question q = new Question();
        q.setTitle(title);
        q.setDescription(description);
        q.setTopic(topic);
        q.setDifficulty(difficulty);
        q.setStarterCode(starterCode);
        q.setExamples(examples);
        q.setConstraints(constraints);
        return questionRepository.save(q);
    }

    private void testCase(Question q, String input, String expectedOutput, boolean hidden) {
        TestCase tc = new TestCase();
        tc.setQuestion(q);
        tc.setInput(input);
        tc.setExpectedOutput(expectedOutput);
        tc.setHidden(hidden);
        testCaseRepository.save(tc);
    }

    private void seedArrays() {
        Question q1 = question(
                "Two Sum",
                "Given a line of space-separated integers and a target on the second line, print the 0-based indices of the two numbers that add up to the target, space-separated, in the order they appear in the array.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int target = Integer.parseInt(sc.nextLine().trim());\n        // TODO: find the two indices whose values sum to target\n    }\n}",
                "Input:\n2 7 11 15\n9\nOutput:\n0 1",
                "2 <= array length <= 10^4, exactly one valid answer exists"
        );
        testCase(q1, "2 7 11 15\n9", "0 1", false);
        testCase(q1, "3 2 4\n6", "1 2", false);
        testCase(q1, "3 3\n6", "0 1", true);

        Question q2 = question(
                "Maximum Subarray",
                "Given a line of space-separated integers, print the largest possible sum of a contiguous subarray.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: Kadane's algorithm\n    }\n}",
                "Input:\n-2 1 -3 4 -1 2 1 -5 4\nOutput:\n6",
                "1 <= array length <= 10^5"
        );
        testCase(q2, "-2 1 -3 4 -1 2 1 -5 4", "6", false);
        testCase(q2, "1", "1", false);
        testCase(q2, "5 4 -1 7 8", "23", true);

        Question q3 = question(
                "Best Time to Buy and Sell Stock",
                "Given a line of space-separated integers representing daily stock prices, print the maximum profit from one buy and one sell (buy before you sell). Print 0 if no profit is possible.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: track the minimum price seen so far and the best profit\n    }\n}",
                "Input:\n7 1 5 3 6 4\nOutput:\n5",
                "1 <= days <= 10^5"
        );
        testCase(q3, "7 1 5 3 6 4", "5", false);
        testCase(q3, "7 6 4 3 1", "0", false);
        testCase(q3, "2 4 1", "2", true);

        Question q4 = question(
                "Contains Duplicate",
                "Given a line of space-separated integers, print true if any value appears at least twice, otherwise print false.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: use a set to detect duplicates\n    }\n}",
                "Input:\n1 2 3 1\nOutput:\ntrue",
                "1 <= array length <= 10^5"
        );
        testCase(q4, "1 2 3 1", "true", false);
        testCase(q4, "1 2 3 4", "false", false);
        testCase(q4, "1 1 1 3 3 4 3 2 4 2", "true", true);

        Question q5 = question(
                "Product of Array Except Self",
                "Given a line of space-separated integers, print an array (space-separated) where each element is the product of all other elements except itself. Do not use division.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: use prefix and suffix product passes\n    }\n}",
                "Input:\n1 2 3 4\nOutput:\n24 12 8 6",
                "2 <= array length <= 10^5"
        );
        testCase(q5, "1 2 3 4", "24 12 8 6", false);
        testCase(q5, "-1 1 0 -3 3", "0 0 9 0 0", false);
        testCase(q5, "2 3", "3 2", true);

        Question q6 = question(
                "Maximum Product Subarray",
                "Given a line of space-separated integers, print the largest product of a contiguous subarray.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: track running max AND min product (negatives flip them)\n    }\n}",
                "Input:\n2 3 -2 4\nOutput:\n6",
                "1 <= array length <= 2*10^4"
        );
        testCase(q6, "2 3 -2 4", "6", false);
        testCase(q6, "-2 0 -1", "0", false);
        testCase(q6, "-2 3 -4", "24", true);

        Question q7 = question(
                "Find Minimum in Rotated Sorted Array",
                "A line of space-separated distinct integers represents a sorted array rotated at some pivot. Print the minimum element.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: binary search for the rotation point\n    }\n}",
                "Input:\n3 4 5 1 2\nOutput:\n1",
                "1 <= array length <= 5000, all values distinct"
        );
        testCase(q7, "3 4 5 1 2", "1", false);
        testCase(q7, "4 5 6 7 0 1 2", "0", false);
        testCase(q7, "11 13 15 17", "11", true);

        Question q8 = question(
                "Search in Rotated Sorted Array",
                "A line of space-separated distinct integers is a sorted array rotated at some pivot. A target is given on the second line. Print the index of the target, or -1 if not present.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int target = Integer.parseInt(sc.nextLine().trim());\n        // TODO: modified binary search\n    }\n}",
                "Input:\n4 5 6 7 0 1 2\n0\nOutput:\n4",
                "1 <= array length <= 5000, all values distinct"
        );
        testCase(q8, "4 5 6 7 0 1 2\n0", "4", false);
        testCase(q8, "4 5 6 7 0 1 2\n3", "-1", false);
        testCase(q8, "1\n0", "-1", true);

        Question q9 = question(
                "3Sum Count",
                "Given a line of space-separated integers, print the number of unique triplets (by value, not index) whose sum is zero.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: sort, then two-pointer for each fixed first element\n    }\n}",
                "Input:\n-1 0 1 2 -1 -4\nOutput:\n2",
                "3 <= array length <= 3000"
        );
        testCase(q9, "-1 0 1 2 -1 -4", "2", false);
        testCase(q9, "0 1 1", "0", false);
        testCase(q9, "0 0 0", "1", true);

        Question q10 = question(
                "Container With Most Water",
                "Given a line of space-separated non-negative integers representing vertical line heights, print the maximum area of water a container formed by two lines and the x-axis can hold.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: two pointers from both ends, move the shorter side\n    }\n}",
                "Input:\n1 8 6 2 5 4 8 3 7\nOutput:\n49",
                "2 <= array length <= 10^5"
        );
        testCase(q10, "1 8 6 2 5 4 8 3 7", "49", false);
        testCase(q10, "1 1", "1", false);
        testCase(q10, "4 3 2 1 4", "16", true);

        Question q11 = question(
                "Trapping Rain Water",
                "Given a line of space-separated non-negative integers representing an elevation map, print the total units of water trapped after rain.",
                Room.Topic.ARRAYS, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: prefix max from left and right, then sum the trapped water\n    }\n}",
                "Input:\n0 1 0 2 1 0 1 3 2 1 2 1\nOutput:\n6",
                "1 <= array length <= 2*10^4"
        );
        testCase(q11, "0 1 0 2 1 0 1 3 2 1 2 1", "6", false);
        testCase(q11, "4 2 0 3 2 5", "9", false);
        testCase(q11, "1 1 1", "0", true);

        Question q12 = question(
                "Merge Intervals",
                "First line: n. Next n lines: two integers 'start end' each, an interval. Print the number of intervals remaining after merging all overlapping ones.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int[][] intervals = new int[n][2];\n        for (int i = 0; i < n; i++) {\n            intervals[i][0] = sc.nextInt();\n            intervals[i][1] = sc.nextInt();\n        }\n        // TODO: merge overlapping intervals, print the resulting count\n    }\n}",
                "Input:\n4\n1 3\n2 6\n8 10\n15 18\nOutput:\n3",
                "1 <= n <= 10^4"
        );
        testCase(q12, "4\n1 3\n2 6\n8 10\n15 18", "3", false);
        testCase(q12, "2\n1 4\n4 5", "1", false);
        testCase(q12, "2\n1 4\n0 4", "1", true);

        Question q13 = question(
                "Non-overlapping Intervals",
                "First line: n. Next n lines: two integers 'start end' each, an interval. Print the minimum number of intervals to remove so the rest don't overlap.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int[][] intervals = new int[n][2];\n        for (int i = 0; i < n; i++) {\n            intervals[i][0] = sc.nextInt();\n            intervals[i][1] = sc.nextInt();\n        }\n        // TODO: greedily pick intervals by earliest end time\n    }\n}",
                "Input:\n4\n1 2\n2 3\n3 4\n1 3\nOutput:\n1",
                "1 <= n <= 10^5"
        );
        testCase(q13, "4\n1 2\n2 3\n3 4\n1 3", "1", false);
        testCase(q13, "3\n1 2\n1 2\n1 2", "2", false);
        testCase(q13, "2\n1 2\n2 3", "0", true);

        Question q14 = question(
                "Two Sum II (Sorted Input)",
                "Given a line of space-separated integers already sorted ascending, and a target on the second line, print the 1-indexed indices of the two numbers that add up to the target.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int target = Integer.parseInt(sc.nextLine().trim());\n        // TODO: two pointers from both ends since the array is sorted\n    }\n}",
                "Input:\n2 7 11 15\n9\nOutput:\n1 2",
                "2 <= array length <= 3*10^4, exactly one valid answer exists"
        );
        testCase(q14, "2 7 11 15\n9", "1 2", false);
        testCase(q14, "2 3 4\n6", "1 3", false);
        testCase(q14, "-1 0\n-1", "1 2", true);

        Question q15 = question(
                "Move Zeroes",
                "Given a line of space-separated integers, print the array with all zeroes moved to the end while keeping the relative order of the non-zero elements.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: shift non-zero elements forward, fill the rest with zero\n    }\n}",
                "Input:\n0 1 0 3 12\nOutput:\n1 3 12 0 0",
                "1 <= array length <= 10^4"
        );
        testCase(q15, "0 1 0 3 12", "1 3 12 0 0", false);
        testCase(q15, "0 0 1", "1 0 0", false);
        testCase(q15, "1 2 3", "1 2 3", true);

        Question q16 = question(
                "Rotate Array",
                "Given a line of space-separated integers and k on the second line, print the array rotated to the right by k steps.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // TODO: rotate the array right by k (mod array length)\n    }\n}",
                "Input:\n1 2 3 4 5 6 7\n3\nOutput:\n5 6 7 1 2 3 4",
                "1 <= array length <= 10^5, 0 <= k <= 10^5"
        );
        testCase(q16, "1 2 3 4 5 6 7\n3", "5 6 7 1 2 3 4", false);
        testCase(q16, "-1 -100 3 99\n2", "3 99 -1 -100", false);
        testCase(q16, "1 2\n3", "2 1", true);

        Question q17 = question(
                "Find All Duplicates in an Array",
                "Given a line of space-separated integers (1 <= value <= n, n = array length), each appearing once or twice, print all values that appear twice, sorted ascending, space-separated.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: mark visited indices, or use a frequency count\n    }\n}",
                "Input:\n4 3 2 7 8 2 3 1\nOutput:\n2 3",
                "1 <= n <= 10^5"
        );
        testCase(q17, "4 3 2 7 8 2 3 1", "2 3", false);
        testCase(q17, "1 1 2", "1", false);
        testCase(q17, "1 2 3", "", true);

        Question q18 = question(
                "Missing Number",
                "Given a line of space-separated integers containing n distinct values in the range [0, n], print the one number missing from the range.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: use the sum formula n*(n+1)/2 minus the actual sum\n    }\n}",
                "Input:\n3 0 1\nOutput:\n2",
                "1 <= n <= 10^4"
        );
        testCase(q18, "3 0 1", "2", false);
        testCase(q18, "0 1", "2", false);
        testCase(q18, "9 6 4 2 3 5 7 0 1", "8", true);

        Question q19 = question(
                "Single Number",
                "Given a line of space-separated integers where every element appears twice except one, print that single one.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: XOR all elements together\n    }\n}",
                "Input:\n2 2 1\nOutput:\n1",
                "1 <= array length <= 3*10^4"
        );
        testCase(q19, "2 2 1", "1", false);
        testCase(q19, "4 1 2 1 2", "4", false);
        testCase(q19, "1", "1", true);

        Question q20 = question(
                "Majority Element",
                "Given a line of space-separated integers where one value appears more than n/2 times, print that value.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: Boyer-Moore voting algorithm\n    }\n}",
                "Input:\n3 2 3\nOutput:\n3",
                "1 <= array length <= 5*10^4"
        );
        testCase(q20, "3 2 3", "3", false);
        testCase(q20, "2 2 1 1 1 2 2", "2", false);
        testCase(q20, "1", "1", true);

        Question q21 = question(
                "Sort Colors",
                "Given a line of space-separated integers, each 0, 1 or 2, print them sorted ascending (the classic Dutch National Flag problem).",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: one-pass three-way partition (or any correct sort)\n    }\n}",
                "Input:\n2 0 2 1 1 0\nOutput:\n0 0 1 1 2 2",
                "1 <= array length <= 300"
        );
        testCase(q21, "2 0 2 1 1 0", "0 0 1 1 2 2", false);
        testCase(q21, "2 0 1", "0 1 2", false);
        testCase(q21, "0", "0", true);

        Question q22 = question(
                "Subarray Sum Equals K",
                "Given a line of space-separated integers and k on the second line, print the number of contiguous subarrays that sum to exactly k.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // TODO: prefix sums with a hashmap of counts\n    }\n}",
                "Input:\n1 1 1\n2\nOutput:\n2",
                "1 <= array length <= 2*10^4"
        );
        testCase(q22, "1 1 1\n2", "2", false);
        testCase(q22, "1 2 3\n3", "2", false);
        testCase(q22, "1\n0", "0", true);

        Question q23 = question(
                "Maximum Circular Subarray Sum",
                "Given a line of space-separated integers representing a circularly connected array (the end wraps to the start), print the maximum possible sum of a non-empty contiguous subarray.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: compare Kadane's normal max against (total - Kadane's min)\n    }\n}",
                "Input:\n1 -2 3 -2\nOutput:\n3",
                "1 <= array length <= 3*10^4"
        );
        testCase(q23, "1 -2 3 -2", "3", false);
        testCase(q23, "5 -3 5", "10", false);
        testCase(q23, "-3 -2 -3", "-2", true);

        Question q24 = question(
                "Next Permutation",
                "Given a line of space-separated integers representing a permutation, print the next lexicographically greater permutation. If it is already the last permutation, print the sorted ascending (smallest) permutation.",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: find the pivot from the right, swap, then reverse the suffix\n    }\n}",
                "Input:\n1 2 3\nOutput:\n1 3 2",
                "1 <= array length <= 100"
        );
        testCase(q24, "1 2 3", "1 3 2", false);
        testCase(q24, "3 2 1", "1 2 3", false);
        testCase(q24, "1 1 5", "1 5 1", true);

        Question q25 = question(
                "Pascal's Triangle Row",
                "Given a single non-negative integer n on one line, print the nth row (0-indexed) of Pascal's Triangle, space-separated.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = Integer.parseInt(sc.nextLine().trim());\n        // TODO: compute row n using the binomial coefficient relation\n    }\n}",
                "Input:\n3\nOutput:\n1 3 3 1",
                "0 <= n <= 33"
        );
        testCase(q25, "3", "1 3 3 1", false);
        testCase(q25, "0", "1", false);
        testCase(q25, "5", "1 5 10 10 5 1", true);

        Question q26 = question(
                "Plus One",
                "Given a line of space-separated single digits representing a non-negative integer (most significant digit first), print the digits of that number plus one, space-separated.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: add one with carry propagation from the last digit\n    }\n}",
                "Input:\n1 2 3\nOutput:\n1 2 4",
                "1 <= digits length <= 100"
        );
        testCase(q26, "1 2 3", "1 2 4", false);
        testCase(q26, "4 3 2 1", "4 3 2 2", false);
        testCase(q26, "9 9", "1 0 0", true);

        Question q27 = question(
                "Find Peak Element",
                "Given a line of space-separated integers, print the index of the leftmost peak element (an element strictly greater than its neighbors; array ends count as -infinity on the missing side).",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: scan left to right for the first index greater than both neighbors\n    }\n}",
                "Input:\n1 2 3 1\nOutput:\n2",
                "1 <= array length <= 1000"
        );
        testCase(q27, "1 2 3 1", "2", false);
        testCase(q27, "1 2 1 3 5 6 4", "1", false);
        testCase(q27, "1", "0", true);

        Question q28 = question(
                "Kth Largest Element",
                "Given a line of space-separated integers and k on the second line, print the kth largest element (1st largest = the maximum).",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // TODO: sort descending and pick index k-1 (or use a heap)\n    }\n}",
                "Input:\n3 2 1 5 6 4\n2\nOutput:\n5",
                "1 <= k <= array length <= 10^4"
        );
        testCase(q28, "3 2 1 5 6 4\n2", "5", false);
        testCase(q28, "3 2 3 1 2 4 5 5 6\n4", "4", false);
        testCase(q28, "1\n1", "1", true);

        Question q29 = question(
                "Merge Two Sorted Arrays",
                "First line: a sorted array of space-separated integers. Second line: another sorted array of space-separated integers. Print the merged, still-sorted result, space-separated.",
                Room.Topic.ARRAYS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] p1 = sc.nextLine().trim().split(\"\\\\s+\");\n        String[] p2 = sc.nextLine().trim().split(\"\\\\s+\");\n        // TODO: merge both sorted arrays into one sorted array\n    }\n}",
                "Input:\n1 2 3\n2 5 6\nOutput:\n1 2 2 3 5 6",
                "0 <= each array length <= 10^4"
        );
        testCase(q29, "1 2 3\n2 5 6", "1 2 2 3 5 6", false);
        testCase(q29, "1 3 5\n2 4 6", "1 2 3 4 5 6", false);
        testCase(q29, "1\n2", "1 2", true);

        Question q30 = question(
                "Longest Consecutive Sequence",
                "Given a line of space-separated integers, print the length of the longest run of consecutive integers (in any order in the input).",
                Room.Topic.ARRAYS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: use a set, only start counting from the start of a run\n    }\n}",
                "Input:\n100 4 200 1 3 2\nOutput:\n4",
                "0 <= array length <= 10^5"
        );
        testCase(q30, "100 4 200 1 3 2", "4", false);
        testCase(q30, "0 3 7 2 5 8 4 6 0 1", "9", false);
        testCase(q30, "1", "1", true);

    }

    private void seedStrings() {
        Question q1 = question(
                "Valid Anagram",
                "Given two lines, each a lowercase string, print true if the second is an anagram of the first, otherwise print false.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String a = sc.nextLine().trim();\n        String b = sc.nextLine().trim();\n        // TODO: check anagram\n    }\n}",
                "Input:\nanagram\nnagaram\nOutput:\ntrue",
                "1 <= length <= 5*10^4, lowercase English letters only"
        );
        testCase(q1, "anagram\nnagaram", "true", false);
        testCase(q1, "rat\ncar", "false", false);
        testCase(q1, "listen\nsilent", "true", true);

        Question q2 = question(
                "Longest Substring Without Repeating Characters",
                "Given a single line string, print the length of the longest substring without repeating characters.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: sliding window with a last-seen-index map\n    }\n}",
                "Input:\nabcabcbb\nOutput:\n3",
                "0 <= length <= 5*10^4"
        );
        testCase(q2, "abcabcbb", "3", false);
        testCase(q2, "bbbbb", "1", false);
        testCase(q2, "pwwkew", "3", true);

        Question q3 = question(
                "Valid Palindrome",
                "Given a single line string, print true if it is a palindrome considering only alphanumeric characters and ignoring case, otherwise print false.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: filter to alphanumeric-lowercased, then compare to its reverse\n    }\n}",
                "Input:\nA man, a plan, a canal: Panama\nOutput:\ntrue",
                "1 <= length <= 2*10^5"
        );
        testCase(q3, "A man, a plan, a canal: Panama", "true", false);
        testCase(q3, "race a car", "false", false);
        testCase(q3, ".,", "true", true);

        Question q4 = question(
                "Longest Palindromic Substring",
                "Given a single line string, print the longest palindromic substring. Test inputs are chosen so the longest palindrome is always unique.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: expand around center for each index (odd and even length)\n    }\n}",
                "Input:\ncbbd\nOutput:\nbb",
                "1 <= length <= 1000"
        );
        testCase(q4, "cbbd", "bb", false);
        testCase(q4, "racecar", "racecar", false);
        testCase(q4, "abacdfgdcaba", "aba", true);

        Question q5 = question(
                "Count Anagram Groups",
                "Given a line of space-separated lowercase words, print the number of distinct anagram groups they form.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] words = sc.nextLine().trim().split(\"\\\\s+\");\n        // TODO: group words by their sorted-character signature, print the group count\n    }\n}",
                "Input:\neat tea tan ate nat bat\nOutput:\n3",
                "1 <= number of words <= 10^4"
        );
        testCase(q5, "eat tea tan ate nat bat", "3", false);
        testCase(q5, "a", "1", false);
        testCase(q5, "ab ba abc", "2", true);

        Question q6 = question(
                "Valid Parentheses",
                "Given a single line string of only the characters ()[]{} , print true if the brackets are balanced and properly nested, otherwise print false.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: use a stack, push opens and match on closes\n    }\n}",
                "Input:\n()[]{}\nOutput:\ntrue",
                "1 <= length <= 10^4"
        );
        testCase(q6, "()[]{}", "true", false);
        testCase(q6, "(]", "false", false);
        testCase(q6, "([)]", "false", true);

        Question q7 = question(
                "Longest Common Prefix",
                "First line: n. Next n lines: one word each. Print the longest common prefix shared by all words (print an empty line if there is none).",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = Integer.parseInt(sc.nextLine().trim());\n        String[] words = new String[n];\n        for (int i = 0; i < n; i++) words[i] = sc.nextLine();\n        // TODO: find the shared prefix, shrinking it as needed\n    }\n}",
                "Input:\n3\nflower\nflow\nflight\nOutput:\nfl",
                "1 <= n <= 200"
        );
        testCase(q7, "3\nflower\nflow\nflight", "fl", false);
        testCase(q7, "3\ndog\nracecar\ncar", "", false);
        testCase(q7, "3\ninterspecies\ninterstellar\ninterstate", "inters", true);

        Question q8 = question(
                "String Compression",
                "Given a single line string, print it run-length encoded: each run of identical characters becomes the character followed by its count (e.g. 'aabcccccaaa' -> 'a2b1c5a3').",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: scan runs of identical consecutive characters\n    }\n}",
                "Input:\naabcccccaaa\nOutput:\na2b1c5a3",
                "1 <= length <= 2000"
        );
        testCase(q8, "aabcccccaaa", "a2b1c5a3", false);
        testCase(q8, "abbbbbbbbbbbb", "a1b12", false);
        testCase(q8, "abc", "a1b1c1", true);

        Question q9 = question(
                "Reverse Words in a String",
                "Given a single line string of words separated by spaces (possibly with extra spaces), print the words in reverse order separated by a single space each, with no leading/trailing spaces.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: split on whitespace, reverse the word list, rejoin\n    }\n}",
                "Input:\nthe sky is blue\nOutput:\nblue is sky the",
                "1 <= length <= 10^4"
        );
        testCase(q9, "the sky is blue", "blue is sky the", false);
        testCase(q9, "  hello world  ", "world hello", false);
        testCase(q9, "a good   example", "example good a", true);

        Question q10 = question(
                "Implement strStr()",
                "First line: haystack string. Second line: needle string. Print the index of the first occurrence of needle in haystack, or -1 if not found.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String haystack = sc.nextLine();\n        String needle = sc.nextLine();\n        // TODO: find the first index where needle occurs in haystack\n    }\n}",
                "Input:\nsadbutsad\nsad\nOutput:\n0",
                "0 <= needle length <= haystack length <= 10^4"
        );
        testCase(q10, "sadbutsad\nsad", "0", false);
        testCase(q10, "leetcode\nleeto", "-1", false);
        testCase(q10, "hello\nll", "2", true);

        Question q11 = question(
                "Longest Common Subsequence",
                "Given two lines of lowercase strings, print the length of their longest common subsequence.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String a = sc.nextLine().trim();\n        String b = sc.nextLine().trim();\n        // TODO: classic O(n*m) dynamic programming table\n    }\n}",
                "Input:\nabcde\nace\nOutput:\n3",
                "1 <= length of each <= 1000"
        );
        testCase(q11, "abcde\nace", "3", false);
        testCase(q11, "abc\nabc", "3", false);
        testCase(q11, "abc\ndef", "0", true);

        Question q12 = question(
                "Edit Distance",
                "Given two lines of lowercase strings, print the minimum number of insert, delete, or replace operations to convert the first string into the second.",
                Room.Topic.STRINGS, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String a = sc.nextLine().trim();\n        String b = sc.nextLine().trim();\n        // TODO: classic Levenshtein distance dynamic programming\n    }\n}",
                "Input:\nhorse\nros\nOutput:\n3",
                "0 <= length of each <= 500"
        );
        testCase(q12, "horse\nros", "3", false);
        testCase(q12, "intention\nexecution", "5", false);
        testCase(q12, "\nabc", "3", true);

        Question q13 = question(
                "Is Subsequence",
                "First line: string s. Second line: string t. Print true if s is a subsequence of t (characters in order, not necessarily contiguous), otherwise print false.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine();\n        String t = sc.nextLine();\n        // TODO: two-pointer walk through t looking for each character of s in order\n    }\n}",
                "Input:\nabc\nahbgdc\nOutput:\ntrue",
                "0 <= |s| <= 100, 0 <= |t| <= 10^4"
        );
        testCase(q13, "abc\nahbgdc", "true", false);
        testCase(q13, "axc\nahbgdc", "false", false);
        testCase(q13, "\nahbgdc", "true", true);

        Question q14 = question(
                "Count and Say",
                "Given a single integer n on one line, print the nth term of the count-and-say sequence (1, 11, 21, 1211, 111221, ...).",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = Integer.parseInt(sc.nextLine().trim());\n        // TODO: build up term by term, describing runs of the previous term\n    }\n}",
                "Input:\n1\nOutput:\n1",
                "1 <= n <= 30"
        );
        testCase(q14, "1", "1", false);
        testCase(q14, "4", "1211", false);
        testCase(q14, "6", "312211", true);

        Question q15 = question(
                "Roman to Integer",
                "Given a single line Roman numeral string, print its integer value.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: sum values, subtracting when a smaller value precedes a larger one\n    }\n}",
                "Input:\nIII\nOutput:\n3",
                "1 <= length <= 15, valid Roman numeral"
        );
        testCase(q15, "III", "3", false);
        testCase(q15, "LVIII", "58", false);
        testCase(q15, "MCMXCIV", "1994", true);

        Question q16 = question(
                "Integer to Roman",
                "Given a single integer on one line (1 to 3999), print its Roman numeral representation.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int num = Integer.parseInt(sc.nextLine().trim());\n        // TODO: greedily subtract the largest matching Roman value/symbol pairs\n    }\n}",
                "Input:\n3\nOutput:\nIII",
                "1 <= num <= 3999"
        );
        testCase(q16, "3", "III", false);
        testCase(q16, "58", "LVIII", false);
        testCase(q16, "1994", "MCMXCIV", true);

        Question q17 = question(
                "First Unique Character",
                "Given a single line lowercase string, print the index of the first character that does not repeat, or -1 if every character repeats.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: count frequencies, then scan for the first count-1 character\n    }\n}",
                "Input:\nleetcode\nOutput:\n0",
                "1 <= length <= 10^5"
        );
        testCase(q17, "leetcode", "0", false);
        testCase(q17, "aabb", "-1", false);
        testCase(q17, "z", "0", true);

        Question q18 = question(
                "Isomorphic Strings",
                "Given two lines of equal-length strings, print true if the characters in the first can be consistently mapped one-to-one to the characters in the second, otherwise print false.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String a = sc.nextLine().trim();\n        String b = sc.nextLine().trim();\n        // TODO: maintain two maps, one each direction, checking consistency\n    }\n}",
                "Input:\negg\nadd\nOutput:\ntrue",
                "1 <= length <= 5*10^4"
        );
        testCase(q18, "egg\nadd", "true", false);
        testCase(q18, "foo\nbar", "false", false);
        testCase(q18, "paper\ntitle", "true", true);

        Question q19 = question(
                "Word Pattern",
                "First line: a pattern string of letters. Second line: a space-separated sequence of words. Print true if the words follow the exact same pattern (bijection between pattern letters and words), else false.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String pattern = sc.nextLine().trim();\n        String s = sc.nextLine().trim();\n        // TODO: maintain two maps (pattern-char -> word, word -> pattern-char)\n    }\n}",
                "Input:\nabba\ndog cat cat dog\nOutput:\ntrue",
                "1 <= pattern length <= 300"
        );
        testCase(q19, "abba\ndog cat cat dog", "true", false);
        testCase(q19, "abba\ndog cat cat fish", "false", false);
        testCase(q19, "aaaa\ndog cat cat dog", "false", true);

        Question q20 = question(
                "Longest Palindromic Subsequence",
                "Given a single line string, print the length of the longest palindromic subsequence (not necessarily contiguous).",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: interval dynamic programming over substrings\n    }\n}",
                "Input:\nbbbab\nOutput:\n4",
                "1 <= length <= 1000"
        );
        testCase(q20, "bbbab", "4", false);
        testCase(q20, "cbbd", "2", false);
        testCase(q20, "a", "1", true);

        Question q21 = question(
                "Multiply Strings",
                "Given two lines, each a non-negative integer represented as a string (may be too large for a normal int/long), print their product as a string.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String num1 = sc.nextLine().trim();\n        String num2 = sc.nextLine().trim();\n        // TODO: simulate grade-school multiplication digit by digit (don't just cast to long)\n    }\n}",
                "Input:\n2\n3\nOutput:\n6",
                "1 <= length of each <= 200"
        );
        testCase(q21, "2\n3", "6", false);
        testCase(q21, "123\n456", "56088", false);
        testCase(q21, "0\n52", "0", true);

        Question q22 = question(
                "Add Binary",
                "Given two lines, each a binary string, print their sum as a binary string.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String a = sc.nextLine().trim();\n        String b = sc.nextLine().trim();\n        // TODO: add with carry from the least significant bit\n    }\n}",
                "Input:\n11\n1\nOutput:\n100",
                "1 <= length of each <= 10^4"
        );
        testCase(q22, "11\n1", "100", false);
        testCase(q22, "1010\n1011", "10101", false);
        testCase(q22, "0\n0", "0", true);

        Question q23 = question(
                "Zigzag Conversion",
                "First line: a string. Second line: numRows. Print the string as read line by line after writing it in a zigzag pattern across numRows rows.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // TODO: simulate the zigzag row assignment, then concatenate rows\n    }\n}",
                "Input:\nPAYPALISHIRING\n3\nOutput:\nPAHNAPLSIIGYIR",
                "1 <= length <= 1000, 1 <= numRows <= 1000"
        );
        testCase(q23, "PAYPALISHIRING\n3", "PAHNAPLSIIGYIR", false);
        testCase(q23, "PAYPALISHIRING\n4", "PINALSIGYAHRPI", false);
        testCase(q23, "AB\n1", "AB", true);

        Question q24 = question(
                "Decode Ways",
                "Given a single line digit string (A=1..Z=26 encoding), print the number of ways it can be decoded into letters.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: dynamic programming: dp[i] depends on the last 1 or 2 digits\n    }\n}",
                "Input:\n12\nOutput:\n2",
                "1 <= length <= 100"
        );
        testCase(q24, "12", "2", false);
        testCase(q24, "226", "3", false);
        testCase(q24, "06", "0", true);

        Question q25 = question(
                "Palindrome Partitioning - Minimum Cuts",
                "Given a single line string, print the minimum number of cuts needed to partition it so every resulting piece is a palindrome.",
                Room.Topic.STRINGS, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: precompute a palindrome table, then dynamic-program over cut counts\n    }\n}",
                "Input:\naab\nOutput:\n1",
                "1 <= length <= 2000"
        );
        testCase(q25, "aab", "1", false);
        testCase(q25, "a", "0", false);
        testCase(q25, "ab", "1", true);

        Question q26 = question(
                "Ransom Note",
                "First line: ransom note string. Second line: magazine string. Print true if the ransom note can be built using only letters available in the magazine (each magazine letter used at most once), else false.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String ransomNote = sc.nextLine();\n        String magazine = sc.nextLine();\n        // TODO: count letters available in magazine, check ransomNote doesn't exceed them\n    }\n}",
                "Input:\na\nb\nOutput:\nfalse",
                "1 <= length of each <= 10^5"
        );
        testCase(q26, "a\nb", "false", false);
        testCase(q26, "aa\naab", "true", false);
        testCase(q26, "aa\nab", "false", true);

        Question q27 = question(
                "Valid Palindrome II",
                "Given a single line lowercase string, print true if it can become a palindrome by removing at most one character, else print false.",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: two pointers; on the first mismatch, try skipping either side\n    }\n}",
                "Input:\naba\nOutput:\ntrue",
                "1 <= length <= 10^5"
        );
        testCase(q27, "aba", "true", false);
        testCase(q27, "abca", "true", false);
        testCase(q27, "abc", "false", true);

        Question q28 = question(
                "Minimum Window Substring Length",
                "First line: string s. Second line: string t. Print the length of the smallest substring of s that contains every character of t (with at least the same multiplicity), or 0 if no such window exists.",
                Room.Topic.STRINGS, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine();\n        String t = sc.nextLine();\n        // TODO: sliding window with a need-count map\n    }\n}",
                "Input:\nADOBECODEBANC\nABC\nOutput:\n4",
                "1 <= |s|, |t| <= 10^5"
        );
        testCase(q28, "ADOBECODEBANC\nABC", "4", false);
        testCase(q28, "a\na", "1", false);
        testCase(q28, "a\naa", "0", true);

        Question q29 = question(
                "Group Shifted Strings Count",
                "Given a line of space-separated lowercase words, two words belong to the same group if every letter can be shifted by the same fixed amount (wrapping z to a) to turn one into the other. Print the number of distinct groups.",
                Room.Topic.STRINGS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] words = sc.nextLine().trim().split(\"\\\\s+\");\n        // TODO: compute each word's shift-signature relative to its first letter, group by it\n    }\n}",
                "Input:\nabc bcd acef xyz az ba a z\nOutput:\n4",
                "1 <= number of words <= 200"
        );
        testCase(q29, "abc bcd acef xyz az ba a z", "4", false);
        testCase(q29, "a b", "1", false);
        testCase(q29, "abc abc", "1", true);

        Question q30 = question(
                "Count Vowels and Consonants",
                "Given a single line string, print two space-separated numbers: the count of vowels, then the count of consonants (letters only, ignore digits/spaces/punctuation).",
                Room.Topic.STRINGS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine().trim();\n        // TODO: scan characters, classify each letter as vowel or consonant\n    }\n}",
                "Input:\nHello World\nOutput:\n3 7",
                "1 <= length <= 10^4"
        );
        testCase(q30, "Hello World", "3 7", false);
        testCase(q30, "xyz", "0 3", false);
        testCase(q30, "AEIOU 123", "5 0", true);

    }

    private void seedLinkedList() {
        Question q1 = question(
                "Reverse a List (array form)",
                "Given a line of space-separated integers representing a linked list, print the values in reverse order, space-separated. (Modeled as an array for simple stdin/stdout judging.)",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: print parts in reverse order, space separated\n    }\n}",
                "Input:\n1 2 3 4 5\nOutput:\n5 4 3 2 1",
                "0 <= list length <= 5000"
        );
        testCase(q1, "1 2 3 4 5", "5 4 3 2 1", false);
        testCase(q1, "1 2", "2 1", false);
        testCase(q1, "7", "7", true);

        Question q2 = question(
                "Detect a Cycle (encoded input)",
                "A line of space-separated integers represents next-pointers by index (-1 means null), starting at index 0. Print true if the list has a cycle, else false.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: Floyd's cycle detection starting at index 0\n    }\n}",
                "Input:\n1 2 0\nOutput:\ntrue",
                "0 <= nodes <= 10^4"
        );
        testCase(q2, "1 2 0", "true", false);
        testCase(q2, "1 -1", "false", false);
        testCase(q2, "1 2 3 -1", "false", true);

        Question q3 = question(
                "Middle of the Linked List",
                "Given a line of space-separated integers, print the value at the middle node. If there are two middle nodes (even length), print the second one.",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: slow/fast pointer walk, or just index len/2\n    }\n}",
                "Input:\n1 2 3 4 5\nOutput:\n3",
                "1 <= list length <= 100"
        );
        testCase(q3, "1 2 3 4 5", "3", false);
        testCase(q3, "1 2 3 4 5 6", "4", false);
        testCase(q3, "1", "1", true);

        Question q4 = question(
                "Remove Nth Node From End",
                "Given a line of space-separated integers and n on the second line, print the list with the nth node from the end removed.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int n = Integer.parseInt(sc.nextLine().trim());\n        // TODO: two-pointer gap technique, or just compute the index directly\n    }\n}",
                "Input:\n1 2 3 4 5\n2\nOutput:\n1 2 3 5",
                "1 <= list length <= 30, 1 <= n <= list length"
        );
        testCase(q4, "1 2 3 4 5\n2", "1 2 3 5", false);
        testCase(q4, "1\n1", "", false);
        testCase(q4, "1 2\n1", "1", true);

        Question q5 = question(
                "Merge Two Sorted Lists",
                "First line: a sorted list of space-separated integers. Second line: another sorted list. Print the merged, still-sorted result.",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] p1 = sc.nextLine().trim().split(\"\\\\s+\");\n        String[] p2 = sc.nextLine().trim().split(\"\\\\s+\");\n        // TODO: merge like the classic merge step of merge sort\n    }\n}",
                "Input:\n1 2 4\n1 3 4\nOutput:\n1 1 2 3 4 4",
                "0 <= each list length <= 50"
        );
        testCase(q5, "1 2 4\n1 3 4", "1 1 2 3 4 4", false);
        testCase(q5, "\n0", "0", false);
        testCase(q5, "5\n1 2 4", "1 2 4 5", true);

        Question q6 = question(
                "Palindrome Linked List",
                "Given a line of space-separated integers, print true if the sequence reads the same forwards and backwards, else false.",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: compare against its own reverse (or use two pointers)\n    }\n}",
                "Input:\n1 2 2 1\nOutput:\ntrue",
                "1 <= list length <= 10^5"
        );
        testCase(q6, "1 2 2 1", "true", false);
        testCase(q6, "1 2", "false", false);
        testCase(q6, "1", "true", true);

        Question q7 = question(
                "Intersection of Two Linked Lists",
                "Two lines, each space-separated integers representing a list. If they share a common tail (suffix), print the length of that shared tail, otherwise print 0.",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] p1 = sc.nextLine().trim().split(\"\\\\s+\");\n        String[] p2 = sc.nextLine().trim().split(\"\\\\s+\");\n        // TODO: compare from the end of both lists backwards while values match\n    }\n}",
                "Input:\n4 1 8 4 5\n5 6 1 8 4 5\nOutput:\n4",
                "1 <= each list length <= 3*10^4"
        );
        testCase(q7, "4 1 8 4 5\n5 6 1 8 4 5", "4", false);
        testCase(q7, "1 2\n3 4", "0", false);
        testCase(q7, "2 6 4\n1 5 6 4", "2", true);

        Question q8 = question(
                "Remove Duplicates from Sorted List",
                "Given a line of space-separated integers already sorted ascending, print the list with consecutive duplicates removed.",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: walk through, skip a value equal to the previous kept one\n    }\n}",
                "Input:\n1 1 2\nOutput:\n1 2",
                "0 <= list length <= 300"
        );
        testCase(q8, "1 1 2", "1 2", false);
        testCase(q8, "1 1 2 3 3", "1 2 3", false);
        testCase(q8, "1", "1", true);

        Question q9 = question(
                "Add Two Numbers",
                "Two lines, each space-separated single digits representing a non-negative integer with the least significant digit FIRST. Print the sum in the same least-significant-digit-first format.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] p1 = sc.nextLine().trim().split(\"\\\\s+\");\n        String[] p2 = sc.nextLine().trim().split(\"\\\\s+\");\n        // TODO: simulate digit-by-digit addition with carry, least-significant digit first\n    }\n}",
                "Input:\n2 4 3\n5 6 4\nOutput:\n7 0 8",
                "1 <= each list length <= 100"
        );
        testCase(q9, "2 4 3\n5 6 4", "7 0 8", false);
        testCase(q9, "0\n0", "0", false);
        testCase(q9, "9 9\n1", "0 0 1", true);

        Question q10 = question(
                "Rotate List",
                "Given a line of space-separated integers and k on the second line, print the list rotated to the right by k places.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // TODO: rotate right by k mod length\n    }\n}",
                "Input:\n1 2 3 4 5\n2\nOutput:\n4 5 1 2 3",
                "0 <= list length <= 500, 0 <= k <= 2*10^9"
        );
        testCase(q10, "1 2 3 4 5\n2", "4 5 1 2 3", false);
        testCase(q10, "0 1 2\n4", "2 0 1", false);
        testCase(q10, "1\n1", "1", true);

        Question q11 = question(
                "Swap Nodes in Pairs",
                "Given a line of space-separated integers, print the list with every pair of adjacent nodes swapped. If there's an odd one out at the end, leave it in place.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: swap values at indices (0,1), (2,3), (4,5), ...\n    }\n}",
                "Input:\n1 2 3 4\nOutput:\n2 1 4 3",
                "0 <= list length <= 100"
        );
        testCase(q11, "1 2 3 4", "2 1 4 3", false);
        testCase(q11, "1 2 3", "2 1 3", false);
        testCase(q11, "1", "1", true);

        Question q12 = question(
                "Odd Even Linked List",
                "Given a line of space-separated integers (1-indexed positions), print all odd-positioned values first (in original order), followed by all even-positioned values (in original order).",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: collect odd-index and even-index elements into two lists, concatenate\n    }\n}",
                "Input:\n1 2 3 4 5\nOutput:\n1 3 5 2 4",
                "0 <= list length <= 10^4"
        );
        testCase(q12, "1 2 3 4 5", "1 3 5 2 4", false);
        testCase(q12, "2 1 3 5 6 4 7", "2 3 6 7 1 5 4", false);
        testCase(q12, "1", "1", true);

        Question q13 = question(
                "Partition List",
                "Given a line of space-separated integers and x on the second line, print the list with all values less than x first (relative order kept), followed by all values >= x (relative order kept).",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int x = Integer.parseInt(sc.nextLine().trim());\n        // TODO: stable-partition into two lists by comparison to x, then concatenate\n    }\n}",
                "Input:\n1 4 3 2 5 2\n3\nOutput:\n1 2 2 4 3 5",
                "0 <= list length <= 200"
        );
        testCase(q13, "1 4 3 2 5 2\n3", "1 2 2 4 3 5", false);
        testCase(q13, "2 1\n2", "1 2", false);
        testCase(q13, "1\n0", "1", true);

        Question q14 = question(
                "Reverse Nodes in k-Group",
                "Given a line of space-separated integers and k on the second line, print the list with every consecutive group of k nodes reversed. A final group smaller than k is left as-is.",
                Room.Topic.LINKED_LIST, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // TODO: reverse each full k-sized chunk, leave the leftover chunk alone\n    }\n}",
                "Input:\n1 2 3 4 5\n2\nOutput:\n2 1 4 3 5",
                "0 <= list length <= 5000, 1 <= k <= list length"
        );
        testCase(q14, "1 2 3 4 5\n2", "2 1 4 3 5", false);
        testCase(q14, "1 2 3 4 5\n3", "3 2 1 4 5", false);
        testCase(q14, "1 2 3 4 5\n1", "1 2 3 4 5", true);

        Question q15 = question(
                "Linked List Cycle II - Find Cycle Start",
                "A line of space-separated integers represents next-pointers by index (-1 means null), starting at index 0. Print the index where the cycle begins, or -1 if there is no cycle.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: Floyd's algorithm: find the meeting point, then find the cycle start\n    }\n}",
                "Input:\n1 2 3 1\nOutput:\n1",
                "0 <= nodes <= 10^4"
        );
        testCase(q15, "1 2 3 1", "1", false);
        testCase(q15, "1 -1", "-1", false);
        testCase(q15, "1 2 0", "0", true);

        Question q16 = question(
                "Sort a Linked List",
                "Given a line of space-separated integers, print them sorted ascending.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: merge sort is the classic O(n log n) approach for a list\n    }\n}",
                "Input:\n4 2 1 3\nOutput:\n1 2 3 4",
                "0 <= list length <= 5*10^4"
        );
        testCase(q16, "4 2 1 3", "1 2 3 4", false);
        testCase(q16, "-1 5 3 4 0", "-1 0 3 4 5", false);
        testCase(q16, "1", "1", true);

        Question q17 = question(
                "Reorder List",
                "Given a line of space-separated integers L0 L1 ... Ln, print them reordered as L0 Ln L1 L(n-1) L2 L(n-2) ...",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: alternate taking from the front and back of the list\n    }\n}",
                "Input:\n1 2 3 4\nOutput:\n1 4 2 3",
                "1 <= list length <= 5*10^4"
        );
        testCase(q17, "1 2 3 4", "1 4 2 3", false);
        testCase(q17, "1 2 3 4 5", "1 5 2 4 3", false);
        testCase(q17, "1", "1", true);

        Question q18 = question(
                "Delete N Nodes After M Nodes",
                "Given a line of space-separated integers, and 'm n' on the second line: keep the first m nodes, delete the next n nodes, and repeat this pattern until the list ends. Print the resulting list.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        String[] mn = sc.nextLine().trim().split(\"\\\\s+\");\n        int m = Integer.parseInt(mn[0]);\n        int n = Integer.parseInt(mn[1]);\n        // TODO: keep m, skip n, repeat, collecting the kept values\n    }\n}",
                "Input:\n1 2 3 4 5 6 7 8 9 10\n2 2\nOutput:\n1 2 5 6 9 10",
                "1 <= list length <= 1000, 1 <= m, n"
        );
        testCase(q18, "1 2 3 4 5 6 7 8 9 10\n2 2", "1 2 5 6 9 10", false);
        testCase(q18, "1 2 3 4 5 6\n3 2", "1 2 3 6", false);
        testCase(q18, "1 2 3\n1 1", "1 3", true);

        Question q19 = question(
                "Length of Linked List",
                "Given a line of space-separated integers, print how many nodes there are.",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: count the elements\n    }\n}",
                "Input:\n1 2 3\nOutput:\n3",
                "0 <= list length <= 10^5"
        );
        testCase(q19, "1 2 3", "3", false);
        testCase(q19, "", "0", false);
        testCase(q19, "5 5 5 5 5", "5", true);

        Question q20 = question(
                "Remove Linked List Elements",
                "Given a line of space-separated integers and val on the second line, print the list with all nodes equal to val removed.",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int val = Integer.parseInt(sc.nextLine().trim());\n        // TODO: filter out every occurrence of val\n    }\n}",
                "Input:\n1 2 6 3 4 5 6\n6\nOutput:\n1 2 3 4 5",
                "0 <= list length <= 10^4"
        );
        testCase(q20, "1 2 6 3 4 5 6\n6", "1 2 3 4 5", false);
        testCase(q20, "\n1", "", false);
        testCase(q20, "7 7 7 7\n7", "", true);

        Question q21 = question(
                "Linked List Components",
                "First line: the linked list values in order. Second line: a set of values G (space-separated). Print the number of connected components formed by consecutive-in-the-list nodes whose value is in G.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] listVals = sc.nextLine().trim().split(\"\\\\s+\");\n        String[] gVals = sc.nextLine().trim().split(\"\\\\s+\");\n        // TODO: walk the list, count maximal runs of consecutive nodes whose value is in G\n    }\n}",
                "Input:\n0 1 2 3\n0 1 3\nOutput:\n2",
                "1 <= list length <= 10^4"
        );
        testCase(q21, "0 1 2 3\n0 1 3", "2", false);
        testCase(q21, "0 1 2 3 4\n0 3 1 4", "2", false);
        testCase(q21, "1 2 3\n1 2 3", "1", true);

        Question q22 = question(
                "Next Greater Node In Linked List",
                "Given a line of space-separated integers, print for each node the value of the next node to its right that is strictly greater, or 0 if there isn't one.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: monotonic decreasing stack of indices\n    }\n}",
                "Input:\n2 1 5\nOutput:\n5 5 0",
                "1 <= list length <= 10^4"
        );
        testCase(q22, "2 1 5", "5 5 0", false);
        testCase(q22, "2 7 4 3 5", "7 0 5 5 0", false);
        testCase(q22, "1 7 5 1 9 2 5 1", "7 9 9 9 0 5 0 0", true);

        Question q23 = question(
                "Split Linked List in Parts",
                "Given a line of space-separated integers and k on the second line, split the list into k consecutive parts as equal in size as possible (earlier parts get any extra nodes). Print the size of each part, space-separated, in order (some may be 0 if k exceeds the list length).",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // TODO: compute base size = length/k, distribute the remainder to the first parts\n    }\n}",
                "Input:\n1 2 3\n5\nOutput:\n1 1 1 0 0",
                "0 <= list length <= 1000, 1 <= k <= 50"
        );
        testCase(q23, "1 2 3\n5", "1 1 1 0 0", false);
        testCase(q23, "1 2 3 4 5 6 7 8 9 10\n3", "4 3 3", false);
        testCase(q23, "1\n1", "1", true);

        Question q24 = question(
                "Josephus Survivor",
                "n people (numbered 1 to n) stand in a circular linked list. Starting at person 1, count off k people each round and eliminate the kth person, continuing around the circle. Print the number (1-indexed) of the last person remaining.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int k = sc.nextInt();\n        // TODO: classic Josephus recurrence: res = (res + k) % i for i = 2..n\n    }\n}",
                "Input:\n5 2\nOutput:\n3",
                "1 <= n <= 5000, 1 <= k <= 5000"
        );
        testCase(q24, "5 2", "3", false);
        testCase(q24, "7 3", "4", false);
        testCase(q24, "1 1", "1", true);

        Question q25 = question(
                "Merge k Sorted Lists",
                "First line: k. Next k lines: each a sorted list of space-separated integers (a line may be empty for an empty list). Print all values merged into one sorted list.",
                Room.Topic.LINKED_LIST, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int k = Integer.parseInt(sc.nextLine().trim());\n        List<int[]> lists = new ArrayList<>();\n        for (int i = 0; i < k; i++) {\n            String line = sc.nextLine().trim();\n            if (line.isEmpty()) { lists.add(new int[0]); continue; }\n            String[] parts = line.split(\"\\\\s+\");\n            int[] vals = new int[parts.length];\n            for (int j = 0; j < parts.length; j++) vals[j] = Integer.parseInt(parts[j]);\n            lists.add(vals);\n        }\n        // TODO: collect all values and sort (or use a min-heap merge)\n    }\n}",
                "Input:\n3\n1 4 5\n1 3 4\n2 6\nOutput:\n1 1 2 3 4 4 5 6",
                "0 <= k <= 10^4"
        );
        testCase(q25, "3\n1 4 5\n1 3 4\n2 6", "1 1 2 3 4 4 5 6", false);
        testCase(q25, "2\n\n", "", false);
        testCase(q25, "2\n\n0", "0", true);

        Question q26 = question(
                "Remove Zero Sum Consecutive Nodes",
                "Given a line of space-separated integers, repeatedly remove consecutive runs of nodes that sum to zero until no such run exists, then print the resulting list. (The final result is provably unique regardless of removal order.)",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: prefix sums: whenever a prefix sum repeats, everything between is zero-sum and can be dropped\n    }\n}",
                "Input:\n1 2 -3 3 1\nOutput:\n3 1",
                "1 <= list length <= 1000"
        );
        testCase(q26, "1 2 -3 3 1", "3 1", false);
        testCase(q26, "1 2 3 -3 4", "1 2 4", false);
        testCase(q26, "1 2 3 -3 -2", "1", true);

        Question q27 = question(
                "Linked List Cycle Length",
                "A line of space-separated integers represents next-pointers by index (-1 means null), starting at index 0. Print the length of the cycle, or 0 if there is none.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: Floyd's algorithm to find the meeting point, then walk around once to measure the cycle\n    }\n}",
                "Input:\n1 2 3 1\nOutput:\n3",
                "0 <= nodes <= 10^4"
        );
        testCase(q27, "1 2 3 1", "3", false);
        testCase(q27, "1 -1", "0", false);
        testCase(q27, "1 2 0", "3", true);

        Question q28 = question(
                "Reverse Only the Second Half",
                "Given a line of space-separated integers, print the list with the first floor(n/2) values unchanged, and the remaining values reversed.",
                Room.Topic.LINKED_LIST, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: split at index n/2, reverse only the second part, concatenate\n    }\n}",
                "Input:\n1 2 3 4 5 6\nOutput:\n1 2 3 6 5 4",
                "1 <= list length <= 1000"
        );
        testCase(q28, "1 2 3 4 5 6", "1 2 3 6 5 4", false);
        testCase(q28, "1 2 3 4 5", "1 2 5 4 3", false);
        testCase(q28, "1", "1", true);

        Question q29 = question(
                "Kth Node From the End",
                "Given a line of space-separated integers and k on the second line (1-indexed from the end), print the value of the kth node from the end.",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // TODO: the answer is at index (length - k)\n    }\n}",
                "Input:\n1 2 3 4 5\n2\nOutput:\n4",
                "1 <= k <= list length <= 10^4"
        );
        testCase(q29, "1 2 3 4 5\n2", "4", false);
        testCase(q29, "1 2 3 4 5\n5", "1", false);
        testCase(q29, "1\n1", "1", true);

        Question q30 = question(
                "Check if Linked List is Sorted",
                "Given a line of space-separated integers, print true if they are in non-decreasing order, else false.",
                Room.Topic.LINKED_LIST, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int[] nums = new int[parts.length];\n        for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n        // TODO: walk through checking each value against the next\n    }\n}",
                "Input:\n1 2 2 3\nOutput:\ntrue",
                "0 <= list length <= 10^4"
        );
        testCase(q30, "1 2 2 3", "true", false);
        testCase(q30, "1 3 2", "false", false);
        testCase(q30, "5", "true", true);

    }

    private void seedTrees() {
        Question q1 = question(
                "Maximum Depth of Binary Tree",
                "Given a line of space-separated integers representing a binary tree in level order (-1 for null), print the maximum depth.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: compute max depth recursively from the level-order array\n    }\n}",
                "Input:\n3 9 20 -1 -1 15 7\nOutput:\n3",
                "0 <= nodes <= 10^4"
        );
        testCase(q1, "3 9 20 -1 -1 15 7", "3", false);
        testCase(q1, "1 -1 2", "2", false);
        testCase(q1, "1", "1", true);

        Question q2 = question(
                "Validate BST",
                "Given a line of space-separated integers representing a binary tree in level order (-1 for null), print true if it's a valid binary search tree, else false.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: validate the BST property with a running (lo, hi) bound\n    }\n}",
                "Input:\n2 1 3\nOutput:\ntrue",
                "0 <= nodes <= 10^4"
        );
        testCase(q2, "2 1 3", "true", false);
        testCase(q2, "5 1 4 -1 -1 3 6", "false", false);
        testCase(q2, "1", "true", true);

        Question q3 = question(
                "Symmetric Tree",
                "Given a line of space-separated integers representing a binary tree in level order (-1 for null), print true if it is a mirror of itself (symmetric around its center), else false.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: recursively compare left and right subtrees as mirrors\n    }\n}",
                "Input:\n1 2 2 3 4 4 3\nOutput:\ntrue",
                "0 <= nodes <= 1000"
        );
        testCase(q3, "1 2 2 3 4 4 3", "true", false);
        testCase(q3, "1 2 2 -1 3 -1 3", "false", false);
        testCase(q3, "1", "true", true);

        Question q4 = question(
                "Invert Binary Tree",
                "Given a line of space-separated integers representing a binary tree in level order (-1 for null), print the level-order serialization (same -1 convention) of the tree with every left/right child swapped.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: recursively swap left and right children, print level order\n    }\n}",
                "Input:\n4 2 7 1 3 6 9\nOutput:\n4 7 2 9 6 3 1",
                "0 <= nodes <= 100"
        );
        testCase(q4, "4 2 7 1 3 6 9", "4 7 2 9 6 3 1", false);
        testCase(q4, "2 1 3", "2 3 1", false);
        testCase(q4, "1", "1", true);

        Question q5 = question(
                "Same Tree",
                "Two lines, each a binary tree in level order (-1 for null). Print true if the two trees are structurally identical with the same values, else false.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] p1 = sc.nextLine().trim().split(\"\\\\s+\");\n        String[] p2 = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout, for both trees\n        // TODO: recursively compare both trees node by node\n    }\n}",
                "Input:\n1 2 3\n1 2 3\nOutput:\ntrue",
                "0 <= nodes in each <= 100"
        );
        testCase(q5, "1 2 3\n1 2 3", "true", false);
        testCase(q5, "1 2\n1 -1 2", "false", false);
        testCase(q5, "1 2 1\n1 1 2", "false", true);

        Question q6 = question(
                "Path Sum",
                "Given a tree (level order, -1 for null) and a target sum on the second line, print true if some root-to-leaf path adds up to exactly the target, else false.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // -1 marks a null node, level-order layout\n        // TODO: DFS subtracting node values, check at leaves\n    }\n}",
                "Input:\n5 4 8 11 -1 13 4 7 2 -1 -1 -1 1\n22\nOutput:\ntrue",
                "0 <= nodes <= 5000"
        );
        testCase(q6, "5 4 8 11 -1 13 4 7 2 -1 -1 -1 1\n22", "true", false);
        testCase(q6, "1 2 3\n5", "false", false);
        testCase(q6, "1\n1", "true", true);

        Question q7 = question(
                "Sum of Left Leaves",
                "Given a tree (level order, -1 for null), print the sum of the values of all leaf nodes that are a LEFT child of their parent.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: DFS, tracking whether the current node is reached via a left edge\n    }\n}",
                "Input:\n3 9 20 -1 -1 15 7\nOutput:\n24",
                "0 <= nodes <= 1000"
        );
        testCase(q7, "3 9 20 -1 -1 15 7", "24", false);
        testCase(q7, "1", "0", false);
        testCase(q7, "1 2 3 4 5", "4", true);

        Question q8 = question(
                "Diameter of Binary Tree",
                "Given a tree (level order, -1 for null), print the diameter: the number of edges on the longest path between any two nodes (the path may or may not pass through the root).",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: DFS returning height, tracking best (leftHeight + rightHeight) globally\n    }\n}",
                "Input:\n1 2 3 4 5\nOutput:\n3",
                "1 <= nodes <= 10^4"
        );
        testCase(q8, "1 2 3 4 5", "3", false);
        testCase(q8, "1 2", "1", false);
        testCase(q8, "1", "0", true);

        Question q9 = question(
                "Balanced Binary Tree Check",
                "Given a tree (level order, -1 for null), print true if it is height-balanced (for every node, the left and right subtree heights differ by at most 1), else false.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: bottom-up height check, short-circuiting to -1 on imbalance\n    }\n}",
                "Input:\n3 9 20 -1 -1 15 7\nOutput:\ntrue",
                "0 <= nodes <= 5000"
        );
        testCase(q9, "3 9 20 -1 -1 15 7", "true", false);
        testCase(q9, "1 2 2 3 3 -1 -1 4 4", "false", false);
        testCase(q9, "1", "true", true);

        Question q10 = question(
                "Lowest Common Ancestor",
                "Given a tree (level order, -1 for null) and two node values 'p q' on the second line (both guaranteed to exist), print the value of their lowest common ancestor.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        String[] pair = sc.nextLine().trim().split(\"\\\\s+\");\n        int a = Integer.parseInt(pair[0]);\n        int b = Integer.parseInt(pair[1]);\n        // -1 marks a null node, level-order layout\n        // TODO: recursive LCA: a node is the LCA if p and q are found in different subtrees\n    }\n}",
                "Input:\n3 5 1 6 2 0 8 -1 -1 7 4\n5 1\nOutput:\n3",
                "2 <= nodes <= 10^4, all values distinct"
        );
        testCase(q10, "3 5 1 6 2 0 8 -1 -1 7 4\n5 1", "3", false);
        testCase(q10, "3 5 1 6 2 0 8 -1 -1 7 4\n5 4", "5", false);
        testCase(q10, "1 2\n1 2", "1", true);

        Question q11 = question(
                "Binary Tree Inorder Traversal",
                "Given a tree (level order, -1 for null), print its inorder traversal (left, root, right), space-separated.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: recursive inorder: left, node, right\n    }\n}",
                "Input:\n1 -1 2 3\nOutput:\n1 3 2",
                "0 <= nodes <= 100"
        );
        testCase(q11, "1 -1 2 3", "1 3 2", false);
        testCase(q11, "1 2 3", "2 1 3", false);
        testCase(q11, "1", "1", true);

        Question q12 = question(
                "Binary Tree Preorder Traversal",
                "Given a tree (level order, -1 for null), print its preorder traversal (root, left, right), space-separated.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: recursive preorder: node, left, right\n    }\n}",
                "Input:\n1 -1 2 3\nOutput:\n1 2 3",
                "0 <= nodes <= 100"
        );
        testCase(q12, "1 -1 2 3", "1 2 3", false);
        testCase(q12, "1 2 3", "1 2 3", false);
        testCase(q12, "1", "1", true);

        Question q13 = question(
                "Binary Tree Postorder Traversal",
                "Given a tree (level order, -1 for null), print its postorder traversal (left, right, root), space-separated.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: recursive postorder: left, right, node\n    }\n}",
                "Input:\n1 -1 2 3\nOutput:\n3 2 1",
                "0 <= nodes <= 100"
        );
        testCase(q13, "1 -1 2 3", "3 2 1", false);
        testCase(q13, "1 2 3", "2 3 1", false);
        testCase(q13, "1", "1", true);

        Question q14 = question(
                "Kth Smallest in BST",
                "Given a valid BST (level order, -1 for null) and k on the second line, print the kth smallest value in the tree.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // -1 marks a null node, level-order layout\n        // TODO: inorder traversal of a BST visits values in sorted order\n    }\n}",
                "Input:\n3 1 4 -1 2\n1\nOutput:\n1",
                "1 <= k <= nodes <= 10^4"
        );
        testCase(q14, "3 1 4 -1 2\n1", "1", false);
        testCase(q14, "5 3 6 2 4 -1 -1 1\n3", "3", false);
        testCase(q14, "1\n1", "1", true);

        Question q15 = question(
                "Count Nodes in Complete Binary Tree",
                "Given a complete binary tree (level order, -1 for null), print the total number of nodes.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: count nodes (a full O(log^2 n) approach exists for complete trees, but a plain count also works)\n    }\n}",
                "Input:\n1 2 3 4 5 6\nOutput:\n6",
                "0 <= nodes <= 5*10^4"
        );
        testCase(q15, "1 2 3 4 5 6", "6", false);
        testCase(q15, "1", "1", false);
        testCase(q15, "", "0", true);

        Question q16 = question(
                "Binary Tree Right Side View",
                "Given a tree (level order, -1 for null), print the values visible when looking at the tree from the right side, ordered top to bottom.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: BFS level by level, keep the last node seen in each level\n    }\n}",
                "Input:\n1 2 3 -1 5 -1 4\nOutput:\n1 3 4",
                "0 <= nodes <= 100"
        );
        testCase(q16, "1 2 3 -1 5 -1 4", "1 3 4", false);
        testCase(q16, "1 -1 3", "1 3", false);
        testCase(q16, "", "", true);

        Question q17 = question(
                "Minimum Depth of Binary Tree",
                "Given a tree (level order, -1 for null), print the length of the shortest path from the root to a leaf node.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: BFS/DFS, careful: a node with only one child is not a leaf\n    }\n}",
                "Input:\n3 9 20 -1 -1 15 7\nOutput:\n2",
                "0 <= nodes <= 10^5"
        );
        testCase(q17, "3 9 20 -1 -1 15 7", "2", false);
        testCase(q17, "2 -1 3 -1 4 -1 5 -1 6", "5", false);
        testCase(q17, "1", "1", true);

        Question q18 = question(
                "Binary Tree Zigzag Level Order Traversal",
                "Given a tree (level order, -1 for null), print its zigzag level order traversal: level 0 left-to-right, level 1 right-to-left, level 2 left-to-right, and so on - all flattened into one space-separated line.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: BFS level by level, reverse alternate levels before appending\n    }\n}",
                "Input:\n3 9 20 -1 -1 15 7\nOutput:\n3 20 9 15 7",
                "0 <= nodes <= 2000"
        );
        testCase(q18, "3 9 20 -1 -1 15 7", "3 20 9 15 7", false);
        testCase(q18, "1", "1", false);
        testCase(q18, "", "", true);

        Question q19 = question(
                "Sum Root to Leaf Numbers",
                "Given a tree (level order, -1 for null) where each node holds a single digit 0-9, each root-to-leaf path forms a number (most significant digit at the root). Print the sum of all such numbers.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: DFS carrying the number built so far, add it up at each leaf\n    }\n}",
                "Input:\n1 2 3\nOutput:\n25",
                "1 <= nodes <= 1000, each value 0-9"
        );
        testCase(q19, "1 2 3", "25", false);
        testCase(q19, "4 9 0 5 1", "1026", false);
        testCase(q19, "0", "0", true);

        Question q20 = question(
                "Flatten Binary Tree to Linked List",
                "Given a tree (level order, -1 for null), flatten it in place into a 'linked list' (each node's right child points to the next node, left always null) following preorder. Print the resulting sequence of values, space-separated.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: the flattened order is exactly preorder traversal - node, left subtree, right subtree\n    }\n}",
                "Input:\n1 2 5 3 4 -1 6\nOutput:\n1 2 3 4 5 6",
                "0 <= nodes <= 2000"
        );
        testCase(q20, "1 2 5 3 4 -1 6", "1 2 3 4 5 6", false);
        testCase(q20, "", "", false);
        testCase(q20, "0", "0", true);

        Question q21 = question(
                "Range Sum of BST",
                "Given a valid BST (level order, -1 for null) and 'low high' on the second line, print the sum of all node values within [low, high] inclusive.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        String[] pair = sc.nextLine().trim().split(\"\\\\s+\");\n        int a = Integer.parseInt(pair[0]);\n        int b = Integer.parseInt(pair[1]);\n        // -1 marks a null node, level-order layout\n        // TODO: use the BST property to prune subtrees entirely outside the range\n    }\n}",
                "Input:\n10 5 15 3 7 -1 18\n7 15\nOutput:\n32",
                "1 <= nodes <= 2*10^4"
        );
        testCase(q21, "10 5 15 3 7 -1 18\n7 15", "32", false);
        testCase(q21, "10 5 15 3 7 13 18 1 -1 6\n6 10", "23", false);
        testCase(q21, "5\n1 10", "5", true);

        Question q22 = question(
                "Delete Node in a BST",
                "Given a valid BST (level order, -1 for null) and a key to delete on the second line, print the inorder traversal of the tree after deleting that key (print the same inorder if the key isn't present).",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // -1 marks a null node, level-order layout\n        // TODO: standard BST deletion (three cases: leaf, one child, two children), then inorder\n    }\n}",
                "Input:\n5 3 6 2 4 -1 7\n3\nOutput:\n2 4 5 6 7",
                "0 <= nodes <= 1000"
        );
        testCase(q22, "5 3 6 2 4 -1 7\n3", "2 4 5 6 7", false);
        testCase(q22, "5 3 6 2 4 -1 7\n0", "2 3 4 5 6 7", false);
        testCase(q22, "5\n5", "", true);

        Question q23 = question(
                "Insert into a BST",
                "Given a valid BST (level order, -1 for null) and a value to insert on the second line, print the preorder traversal of the tree after inserting the value as a new leaf.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        int k = Integer.parseInt(sc.nextLine().trim());\n        // -1 marks a null node, level-order layout\n        // TODO: walk left/right by comparison until an empty spot, insert there, then preorder\n    }\n}",
                "Input:\n4 2 7 1 3\n5\nOutput:\n4 2 1 3 7 5",
                "0 <= nodes <= 1000"
        );
        testCase(q23, "4 2 7 1 3\n5", "4 2 1 3 7 5", false);
        testCase(q23, "40 20 60 10 30 50 70\n25", "40 20 10 30 25 60 50 70", false);
        testCase(q23, "\n1", "1", true);

        Question q24 = question(
                "Count Leaves in Binary Tree",
                "Given a tree (level order, -1 for null), print the number of leaf nodes (nodes with no children).",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: recursively count nodes with no left and no right child\n    }\n}",
                "Input:\n1 2 3 4 5\nOutput:\n3",
                "0 <= nodes <= 10^4"
        );
        testCase(q24, "1 2 3 4 5", "3", false);
        testCase(q24, "1", "1", false);
        testCase(q24, "", "0", true);

        Question q25 = question(
                "Count Full Nodes",
                "Given a tree (level order, -1 for null), print the number of 'full' nodes: nodes that have exactly two children.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: recursively count nodes where both left and right children exist\n    }\n}",
                "Input:\n1 2 3 4 5\nOutput:\n2",
                "0 <= nodes <= 10^4"
        );
        testCase(q25, "1 2 3 4 5", "2", false);
        testCase(q25, "1 2", "0", false);
        testCase(q25, "1", "0", true);

        Question q26 = question(
                "Average of Levels in Binary Tree",
                "Given a tree (level order, -1 for null), print the average value of each level, top to bottom, space-separated, each formatted to exactly 2 decimal places (e.g. '3.00 14.50').",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: BFS level by level, sum/count each level, format each average with String.format(\"%.2f\", avg)\n    }\n}",
                "Input:\n3 9 20 -1 -1 15 7\nOutput:\n3.00 14.50 11.00",
                "1 <= nodes <= 10^4"
        );
        testCase(q26, "3 9 20 -1 -1 15 7", "3.00 14.50 11.00", false);
        testCase(q26, "3 9 20", "3.00 14.50", false);
        testCase(q26, "5", "5.00", true);

        Question q27 = question(
                "Binary Tree Maximum Path Sum",
                "Given a tree (level order, -1 for null), print the maximum path sum where a path is any sequence of nodes connected by edges, starting and ending anywhere (does not have to pass through the root).",
                Room.Topic.TREES, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: DFS returning best downward sum, tracking best node+left+right globally\n    }\n}",
                "Input:\n1 2 3\nOutput:\n6",
                "1 <= nodes <= 3*10^4"
        );
        testCase(q27, "1 2 3", "6", false);
        testCase(q27, "-10 9 20 -1 -1 15 7", "42", false);
        testCase(q27, "-3", "-3", true);

        Question q28 = question(
                "Level with Maximum Sum",
                "Given a tree (level order, -1 for null), print the 0-indexed level number whose node values sum to the largest total. Test inputs are chosen so there is always a single unique maximum level.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: BFS level by level, sum each level, track the index of the largest sum\n    }\n}",
                "Input:\n1 7 0 7 -8 -1 -1\nOutput:\n1",
                "1 <= nodes <= 10^4"
        );
        testCase(q28, "1 7 0 7 -8 -1 -1", "1", false);
        testCase(q28, "989 -10086 1 -1 -1 8983 7539", "2", false);
        testCase(q28, "1 2 3", "1", true);

        Question q29 = question(
                "Cousins in Binary Tree",
                "Given a tree (level order, -1 for null) and 'x y' on the second line (two distinct values that exist in the tree), print true if x and y are cousins (same depth, different parents), else false.",
                Room.Topic.TREES, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        String[] pair = sc.nextLine().trim().split(\"\\\\s+\");\n        int a = Integer.parseInt(pair[0]);\n        int b = Integer.parseInt(pair[1]);\n        // -1 marks a null node, level-order layout\n        // TODO: find each node's depth and parent, compare\n    }\n}",
                "Input:\n1 2 3 4 -1 -1 5\n4 5\nOutput:\ntrue",
                "2 <= nodes <= 100, all values distinct"
        );
        testCase(q29, "1 2 3 4 -1 -1 5\n4 5", "true", false);
        testCase(q29, "1 2 3 -1 4 -1 5\n2 3", "false", false);
        testCase(q29, "1 2 3 4 5 6 7\n4 6", "true", true);

        Question q30 = question(
                "Check Complete Binary Tree",
                "Given a tree (level order, -1 for null), print true if every level is completely filled except possibly the last, which is filled left to right with no gaps, else false.",
                Room.Topic.TREES, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String[] parts = sc.nextLine().trim().split(\"\\\\s+\");\n        // -1 marks a null node, level-order layout\n        // TODO: BFS including null children; once a null is seen, no real node may follow\n    }\n}",
                "Input:\n1 2 3 4 5 6\nOutput:\ntrue",
                "1 <= nodes <= 1000"
        );
        testCase(q30, "1 2 3 4 5 6", "true", false);
        testCase(q30, "1 2 3 -1 4 -1 6", "false", false);
        testCase(q30, "1", "true", true);

    }

    private void seedGraphs() {
        Question q1 = question(
                "Number of Connected Components",
                "First line: n (number of nodes, 0-indexed) and e (number of edges). Next e lines: two integers, an undirected edge. Print the number of connected components.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: union-find or DFS to count components\n    }\n}",
                "Input:\n5 3\n0 1\n1 2\n3 4\nOutput:\n2",
                "0 <= n <= 10^4"
        );
        testCase(q1, "5 3\n0 1\n1 2\n3 4", "2", false);
        testCase(q1, "4 0", "4", false);
        testCase(q1, "3 3\n0 1\n1 2\n0 2", "1", true);

        Question q2 = question(
                "Shortest Path (unweighted)",
                "First line: n (nodes, 0-indexed), e (edges), src, dst. Next e lines: two integers, an undirected edge. Print the length of the shortest path from src to dst in number of edges, or -1 if unreachable.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int src = sc.nextInt();\n        int dst = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: BFS\n    }\n}",
                "Input:\n5 4 0 4\n0 1\n1 2\n2 3\n3 4\nOutput:\n4",
                "0 <= n <= 10^4"
        );
        testCase(q2, "5 4 0 4\n0 1\n1 2\n2 3\n3 4", "4", false);
        testCase(q2, "3 1 0 2\n0 1", "-1", false);
        testCase(q2, "2 1 0 1\n0 1", "1", true);

        Question q3 = question(
                "Detect Cycle in Undirected Graph",
                "First line: n, e. Next e lines: two integers, an undirected edge. Print true if the graph contains a cycle, else false.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: union-find: a cycle exists if an edge connects two already-unioned nodes\n    }\n}",
                "Input:\n4 4\n0 1\n1 2\n2 3\n3 0\nOutput:\ntrue",
                "0 <= n <= 10^4"
        );
        testCase(q3, "4 4\n0 1\n1 2\n2 3\n3 0", "true", false);
        testCase(q3, "4 3\n0 1\n1 2\n2 3", "false", false);
        testCase(q3, "1 0", "false", true);

        Question q4 = question(
                "Detect Cycle in Directed Graph",
                "First line: n, e. Next e lines: two integers 'u v' meaning a directed edge u -> v. Print true if the graph contains a cycle, else false.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: DFS with a 3-color (unvisited/in-progress/done) state array\n    }\n}",
                "Input:\n3 3\n0 1\n1 2\n2 0\nOutput:\ntrue",
                "0 <= n <= 10^4"
        );
        testCase(q4, "3 3\n0 1\n1 2\n2 0", "true", false);
        testCase(q4, "3 2\n0 1\n1 2", "false", false);
        testCase(q4, "2 2\n0 1\n1 0", "true", true);

        Question q5 = question(
                "Bipartite Check",
                "First line: n, e. Next e lines: an undirected edge. Print true if the graph can be 2-colored so no edge connects same-colored nodes, else false.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: BFS 2-coloring, checking for conflicts\n    }\n}",
                "Input:\n4 4\n0 1\n1 2\n2 3\n3 0\nOutput:\ntrue",
                "0 <= n <= 10^4"
        );
        testCase(q5, "4 4\n0 1\n1 2\n2 3\n3 0", "true", false);
        testCase(q5, "3 3\n0 1\n1 2\n2 0", "false", false);
        testCase(q5, "2 0", "true", true);

        Question q6 = question(
                "Count Islands",
                "First line: rows, cols. Next rows lines: cols space-separated 0/1 values (a grid). Print the number of islands (groups of 1s connected up/down/left/right).",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int rows = sc.nextInt();\n        int cols = sc.nextInt();\n        int[][] grid = new int[rows][cols];\n        for (int r = 0; r < rows; r++)\n            for (int c = 0; c < cols; c++)\n                grid[r][c] = sc.nextInt();\n        // TODO: flood fill (DFS/BFS) from each unvisited land cell, counting fills\n    }\n}",
                "Input:\n4 5\n1 1 0 0 0\n1 1 0 0 0\n0 0 1 0 0\n0 0 0 1 1\nOutput:\n3",
                "1 <= rows, cols <= 300"
        );
        testCase(q6, "4 5\n1 1 0 0 0\n1 1 0 0 0\n0 0 1 0 0\n0 0 0 1 1", "3", false);
        testCase(q6, "2 2\n1 0\n0 1", "2", false);
        testCase(q6, "2 2\n0 0\n0 0", "0", true);

        Question q7 = question(
                "Number of Provinces",
                "First line: n. Next n lines: n space-separated 0/1 values, an adjacency matrix where matrix[i][j]=1 means city i and j are directly connected. Print the number of provinces (connected groups of cities).",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int[][] matrix = new int[n][n];\n        for (int i = 0; i < n; i++)\n            for (int j = 0; j < n; j++)\n                matrix[i][j] = sc.nextInt();\n        // TODO: union-find or DFS over the adjacency matrix\n    }\n}",
                "Input:\n3\n1 1 0\n1 1 0\n0 0 1\nOutput:\n2",
                "1 <= n <= 200"
        );
        testCase(q7, "3\n1 1 0\n1 1 0\n0 0 1", "2", false);
        testCase(q7, "3\n1 0 0\n0 1 0\n0 0 1", "3", false);
        testCase(q7, "1\n1", "1", true);

        Question q8 = question(
                "Course Schedule",
                "First line: n (courses 0..n-1), e (prerequisite pairs). Next e lines: 'a b' meaning course b must be taken before course a. Print true if all n courses can be finished (no cyclic dependency), else false.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: Kahn's algorithm (BFS topological sort using in-degrees)\n    }\n}",
                "Input:\n2 1\n1 0\nOutput:\ntrue",
                "1 <= n <= 2000"
        );
        testCase(q8, "2 1\n1 0", "true", false);
        testCase(q8, "2 2\n1 0\n0 1", "false", false);
        testCase(q8, "1 0", "true", true);

        Question q9 = question(
                "Topological Sort Order",
                "First line: n, e. Next e lines: 'u v' meaning a directed edge u -> v (u must come before v). Print a valid topological order, space-separated. To make the answer unique, always expand the smallest available node id first (Kahn's algorithm with a min-priority-queue of ready nodes). If a cycle makes topological order impossible, print CYCLE.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: Kahn's algorithm using a MIN-HEAP of ready (in-degree 0) nodes, not a plain queue\n    }\n}",
                "Input:\n4 4\n0 1\n0 2\n1 3\n2 3\nOutput:\n0 1 2 3",
                "1 <= n <= 1000"
        );
        testCase(q9, "4 4\n0 1\n0 2\n1 3\n2 3", "0 1 2 3", false);
        testCase(q9, "3 3\n0 1\n1 2\n2 0", "CYCLE", false);
        testCase(q9, "3 0", "0 1 2", true);

        Question q10 = question(
                "Word Ladder Length",
                "First line: beginWord. Second line: endWord. Third line: space-separated word list (the allowed dictionary). Each step changes exactly one letter and the result must be in the word list. Print the number of words in the shortest transformation sequence from beginWord to endWord (including both ends), or 0 if impossible.",
                Room.Topic.GRAPHS, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String beginWord = sc.nextLine().trim();\n        String endWord = sc.nextLine().trim();\n        String[] wordList = sc.nextLine().trim().split(\"\\\\s+\");\n        // TODO: BFS over the word graph, trying every single-letter change at each step\n    }\n}",
                "Input:\nhit\ncog\nhot dot dog lot log cog\nOutput:\n5",
                "1 <= word length <= 10, 1 <= word list size <= 5000"
        );
        testCase(q10, "hit\ncog\nhot dot dog lot log cog", "5", false);
        testCase(q10, "hit\ncog\nhot dot dog lot log", "0", false);
        testCase(q10, "a\nc\na b c", "2", true);

        Question q11 = question(
                "Graph Valid Tree",
                "First line: n, e. Next e lines: an undirected edge. Print true if these edges form a valid tree (connected, no cycles), else false.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: a valid tree needs exactly n-1 edges AND no cycle (union-find)\n    }\n}",
                "Input:\n5 4\n0 1\n0 2\n0 3\n1 4\nOutput:\ntrue",
                "1 <= n <= 2000"
        );
        testCase(q11, "5 4\n0 1\n0 2\n0 3\n1 4", "true", false);
        testCase(q11, "5 5\n0 1\n1 2\n2 3\n1 3\n1 4", "false", false);
        testCase(q11, "1 0", "true", true);

        Question q12 = question(
                "Find the Town Judge",
                "First line: n (people numbered 1..n), t (trust relationships). Next t lines: 'a b' meaning a trusts b. The town judge trusts nobody but is trusted by everyone else. Print the judge's number, or -1 if none.",
                Room.Topic.GRAPHS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int t = sc.nextInt();\n        int[][] trust = new int[t][2];\n        for (int i = 0; i < t; i++) {\n            trust[i][0] = sc.nextInt();\n            trust[i][1] = sc.nextInt();\n        }\n        // TODO: count out-degree and in-degree for each person, find the one with 0 out and n-1 in\n    }\n}",
                "Input:\n2 1\n1 2\nOutput:\n2",
                "1 <= n <= 1000"
        );
        testCase(q12, "2 1\n1 2", "2", false);
        testCase(q12, "3 2\n1 3\n2 3", "3", false);
        testCase(q12, "3 3\n1 3\n2 3\n3 1", "-1", true);

        Question q13 = question(
                "Minimum Spanning Tree Weight",
                "First line: n, e. Next e lines: 'u v w' an undirected weighted edge. Print the total weight of the minimum spanning tree, or -1 if the graph isn't connected.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][3]; // u, v, weight\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n            edges[i][2] = sc.nextInt();\n        }\n        // TODO: Kruskal's algorithm: sort edges by weight, union-find to avoid cycles\n    }\n}",
                "Input:\n4 4\n0 1 1\n1 2 2\n2 3 3\n0 3 4\nOutput:\n6",
                "1 <= n <= 2000"
        );
        testCase(q13, "4 4\n0 1 1\n1 2 2\n2 3 3\n0 3 4", "6", false);
        testCase(q13, "3 1\n0 1 5", "-1", false);
        testCase(q13, "2 1\n0 1 10", "10", true);

        Question q14 = question(
                "Dijkstra Shortest Path",
                "First line: n, e, src, dst. Next e lines: 'u v w', an undirected weighted edge (w >= 0). Print the shortest distance from src to dst, or -1 if unreachable.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int src = sc.nextInt();\n        int dst = sc.nextInt();\n        int[][] edges = new int[e][3]; // u, v, weight\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n            edges[i][2] = sc.nextInt();\n        }\n        // TODO: Dijkstra's algorithm with a min-heap\n    }\n}",
                "Input:\n5 6 0 4\n0 1 4\n0 2 1\n2 1 2\n1 3 1\n2 3 5\n3 4 3\nOutput:\n7",
                "1 <= n <= 2000, 0 <= w <= 1000"
        );
        testCase(q14, "5 6 0 4\n0 1 4\n0 2 1\n2 1 2\n1 3 1\n2 3 5\n3 4 3", "7", false);
        testCase(q14, "3 1 0 2\n0 1 1", "-1", false);
        testCase(q14, "2 1 0 1\n0 1 7", "7", true);

        Question q15 = question(
                "Flood Fill",
                "First line: rows, cols. Next rows lines: the grid (space-separated ints). Last line: 'sr sc newColor', the starting cell and fill color. Print the resulting grid flattened row by row, space-separated.",
                Room.Topic.GRAPHS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int rows = sc.nextInt();\n        int cols = sc.nextInt();\n        int[][] grid = new int[rows][cols];\n        for (int r = 0; r < rows; r++)\n            for (int c = 0; c < cols; c++)\n                grid[r][c] = sc.nextInt();\n        int sr = sc.nextInt();\n        int sc2 = sc.nextInt();\n        int newColor = sc.nextInt();\n        // TODO: DFS/BFS filling all 4-directionally connected cells matching the start color\n    }\n}",
                "Input:\n3 3\n1 1 1\n1 1 0\n1 0 1\n1 1 2\nOutput:\n2 2 2 2 2 0 2 0 1",
                "1 <= rows, cols <= 50"
        );
        testCase(q15, "3 3\n1 1 1\n1 1 0\n1 0 1\n1 1 2", "2 2 2 2 2 0 2 0 1", false);
        testCase(q15, "2 3\n0 0 0\n0 0 0\n0 0 0", "0 0 0 0 0 0", false);
        testCase(q15, "1 1\n1\n0 0 5", "5", true);

        Question q16 = question(
                "Rotting Oranges",
                "First line: rows, cols. Next rows lines: grid values (0=empty, 1=fresh orange, 2=rotten orange). Each minute, every rotten orange rots its 4-directionally adjacent fresh oranges. Print the number of minutes until no fresh orange remains, or -1 if impossible.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int rows = sc.nextInt();\n        int cols = sc.nextInt();\n        int[][] grid = new int[rows][cols];\n        for (int r = 0; r < rows; r++)\n            for (int c = 0; c < cols; c++)\n                grid[r][c] = sc.nextInt();\n        // TODO: multi-source BFS starting from all rotten oranges simultaneously\n    }\n}",
                "Input:\n3 3\n2 1 1\n1 1 0\n0 1 1\nOutput:\n4",
                "1 <= rows, cols <= 10"
        );
        testCase(q16, "3 3\n2 1 1\n1 1 0\n0 1 1", "4", false);
        testCase(q16, "3 3\n2 1 1\n0 1 1\n1 0 1", "-1", false);
        testCase(q16, "1 2\n0 2", "0", true);

        Question q17 = question(
                "Redundant Connection",
                "First line: n, e (e = n, since this graph is a tree plus exactly one extra edge, nodes 1-indexed). Next e lines: an undirected edge. Print the extra edge that can be removed to make it a tree again - specifically, the LAST edge (in input order) that would complete a cycle when adding edges one at a time with union-find.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: process edges in order with union-find; the first union() that fails is the answer\n    }\n}",
                "Input:\n3 3\n1 2\n1 3\n2 3\nOutput:\n2 3",
                "3 <= n <= 1000"
        );
        testCase(q17, "3 3\n1 2\n1 3\n2 3", "2 3", false);
        testCase(q17, "4 4\n1 2\n2 3\n3 4\n1 4", "1 4", false);
        testCase(q17, "3 3\n1 2\n2 3\n1 3", "1 3", true);

        Question q18 = question(
                "Shortest Path in Binary Matrix",
                "First line: n, n (a square grid). Next n lines: grid values (0=open, 1=blocked). Moving in any of 8 directions, print the length (number of cells visited) of the shortest path from top-left to bottom-right through open cells only, or -1 if impossible.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int rows = sc.nextInt();\n        int cols = sc.nextInt();\n        int[][] grid = new int[rows][cols];\n        for (int r = 0; r < rows; r++)\n            for (int c = 0; c < cols; c++)\n                grid[r][c] = sc.nextInt();\n        // TODO: BFS with all 8 directions\n    }\n}",
                "Input:\n2 2\n0 1\n1 0\nOutput:\n2",
                "1 <= n <= 100"
        );
        testCase(q18, "2 2\n0 1\n1 0", "2", false);
        testCase(q18, "3 3\n0 0 0\n1 1 0\n1 1 0", "4", false);
        testCase(q18, "3 3\n1 0 0\n1 1 0\n1 1 0", "-1", true);

        Question q19 = question(
                "Minimum Height Trees - Centroid Count",
                "First line: n, e (this graph is guaranteed to be a tree, e = n-1). Next e lines: an undirected edge. If you root the tree at each possible node, some choices minimize the tree's height. Print how many such optimal root choices exist (always 1 or 2).",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: repeatedly strip leaves layer by layer (like topological peeling) until 1 or 2 nodes remain\n    }\n}",
                "Input:\n4 3\n1 0\n1 2\n1 3\nOutput:\n1",
                "1 <= n <= 2*10^4"
        );
        testCase(q19, "4 3\n1 0\n1 2\n1 3", "1", false);
        testCase(q19, "6 5\n3 0\n3 1\n3 2\n3 4\n5 4", "2", false);
        testCase(q19, "1 0", "1", true);

        Question q20 = question(
                "Network Delay Time",
                "First line: n (nodes 1..n), e, k (the signal's source). Next e lines: 'u v w', a DIRECTED weighted edge (signal travel time). Print the time for the signal to reach every node, or -1 if some node is unreachable.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int k = sc.nextInt();\n        int[][] edges = new int[e][3]; // u, v, weight (directed u -> v)\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n            edges[i][2] = sc.nextInt();\n        }\n        // TODO: Dijkstra from k, answer is the max finite distance to any node\n    }\n}",
                "Input:\n4 3 2\n2 1 1\n2 3 1\n3 4 1\nOutput:\n2",
                "1 <= n <= 100"
        );
        testCase(q20, "4 3 2\n2 1 1\n2 3 1\n3 4 1", "2", false);
        testCase(q20, "2 1 2\n1 2 1", "-1", false);
        testCase(q20, "1 0 1", "0", true);

        Question q21 = question(
                "Cheapest Flights Within K Stops",
                "First line: n (cities 0..n-1), e, src, dst, k (max stops allowed). Next e lines: 'u v w', a DIRECTED flight u -> v costing w. Print the cheapest price from src to dst using at most k stops (k+1 flights), or -1 if impossible.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int src = sc.nextInt();\n        int dst = sc.nextInt();\n        int k = sc.nextInt();\n        int[][] edges = new int[e][3]; // u, v, price (directed u -> v)\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n            edges[i][2] = sc.nextInt();\n        }\n        // TODO: Bellman-Ford limited to k+1 relaxation rounds (Dijkstra doesn't respect the stop limit correctly)\n    }\n}",
                "Input:\n4 5 0 3 1\n0 1 100\n1 2 100\n2 0 100\n1 3 600\n2 3 200\nOutput:\n700",
                "1 <= n <= 100"
        );
        testCase(q21, "4 5 0 3 1\n0 1 100\n1 2 100\n2 0 100\n1 3 600\n2 3 200", "700", false);
        testCase(q21, "3 3 0 2 1\n0 1 100\n1 2 100\n0 2 500", "200", false);
        testCase(q21, "3 3 0 2 0\n0 1 100\n1 2 100\n0 2 500", "500", true);

        Question q22 = question(
                "Keys and Rooms",
                "First line: n (rooms 0..n-1, you start in room 0, unlocked). Next n lines: room i's line lists the keys (room numbers) found inside it (may be empty). Print true if every room can eventually be visited, else false.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = Integer.parseInt(sc.nextLine().trim());\n        List<List<Integer>> rooms = new ArrayList<>();\n        for (int i = 0; i < n; i++) {\n            String line = sc.nextLine().trim();\n            List<Integer> keys = new ArrayList<>();\n            if (!line.isEmpty()) {\n                String[] parts = line.split(\"\\\\s+\");\n                for (String p : parts) keys.add(Integer.parseInt(p));\n            }\n            rooms.add(keys);\n        }\n        // TODO: DFS/BFS from room 0 following keys found, track visited rooms\n    }\n}",
                "Input:\n4\n1\n2\n3\n\nOutput:\ntrue",
                "1 <= n <= 1000"
        );
        testCase(q22, "4\n1\n2\n3\n", "true", false);
        testCase(q22, "4\n1 3\n3 0 1\n2\n0", "false", false);
        testCase(q22, "1\n", "true", true);

        Question q23 = question(
                "Find if Path Exists in Graph",
                "First line: n, e, src, dst. Next e lines: an undirected edge. Print true if there is a path from src to dst, else false.",
                Room.Topic.GRAPHS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int src = sc.nextInt();\n        int dst = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: union-find, or a simple BFS/DFS\n    }\n}",
                "Input:\n3 2 0 2\n0 1\n1 2\nOutput:\ntrue",
                "1 <= n <= 2*10^5"
        );
        testCase(q23, "3 2 0 2\n0 1\n1 2", "true", false);
        testCase(q23, "6 3 0 5\n0 1\n2 3\n3 4", "false", false);
        testCase(q23, "1 0 0 0", "true", true);

        Question q24 = question(
                "Find Center of Star Graph",
                "First line: n, e (a star graph: one center node connected to all n-1 others, e = n-1 edges). Next e lines: an undirected edge. Print the center node's number.",
                Room.Topic.GRAPHS, Question.Difficulty.EASY,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: the center node appears in both of the first two edges\n    }\n}",
                "Input:\n4 3\n1 2\n2 3\n4 2\nOutput:\n2",
                "3 <= n <= 10^5"
        );
        testCase(q24, "4 3\n1 2\n2 3\n4 2", "2", false);
        testCase(q24, "5 4\n1 2\n5 1\n1 3\n1 4", "1", false);
        testCase(q24, "3 2\n2 1\n1 3", "1", true);

        Question q25 = question(
                "Number of Operations to Make Network Connected",
                "First line: n (computers 0..n-1), e (existing cables). Next e lines: an undirected edge (an existing cable). Print the minimum number of cable moves to connect all computers, or -1 if there aren't enough cables to do it at all.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: need at least n-1 cables total; otherwise answer is (number of connected components - 1)\n    }\n}",
                "Input:\n4 3\n0 1\n0 2\n1 2\nOutput:\n1",
                "1 <= n <= 10^5"
        );
        testCase(q25, "4 3\n0 1\n0 2\n1 2", "1", false);
        testCase(q25, "6 5\n0 1\n0 2\n0 3\n1 2\n1 3", "2", false);
        testCase(q25, "6 1\n0 1", "-1", true);

        Question q26 = question(
                "Critical Connections (Bridges) Count",
                "First line: n, e. Next e lines: an undirected edge. Print the number of bridges: edges whose removal would disconnect the graph.",
                Room.Topic.GRAPHS, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: Tarjan's bridge-finding algorithm using discovery time and low-link values\n    }\n}",
                "Input:\n4 4\n0 1\n1 2\n2 0\n1 3\nOutput:\n1",
                "1 <= n <= 10^5"
        );
        testCase(q26, "4 4\n0 1\n1 2\n2 0\n1 3", "1", false);
        testCase(q26, "2 1\n0 1", "1", false);
        testCase(q26, "4 3\n0 1\n1 2\n2 3", "3", true);

        Question q27 = question(
                "Articulation Points Count",
                "First line: n, e. Next e lines: an undirected edge. Print the number of articulation points (cut vertices): nodes whose removal increases the number of connected components.",
                Room.Topic.GRAPHS, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: Tarjan's articulation point algorithm (discovery time / low-link, careful root special case)\n    }\n}",
                "Input:\n5 5\n0 1\n1 2\n2 0\n1 3\n3 4\nOutput:\n2",
                "1 <= n <= 10^5"
        );
        testCase(q27, "5 5\n0 1\n1 2\n2 0\n1 3\n3 4", "2", false);
        testCase(q27, "2 1\n0 1", "0", false);
        testCase(q27, "4 3\n0 1\n1 2\n2 3", "2", true);

        Question q28 = question(
                "Count Strongly Connected Components",
                "First line: n, e. Next e lines: 'u v' meaning a DIRECTED edge u -> v. Print the number of strongly connected components.",
                Room.Topic.GRAPHS, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: Kosaraju's algorithm: DFS finish-order, reverse the graph, DFS again in that order\n    }\n}",
                "Input:\n5 5\n0 1\n1 2\n2 0\n2 3\n3 4\nOutput:\n3",
                "1 <= n <= 5000"
        );
        testCase(q28, "5 5\n0 1\n1 2\n2 0\n2 3\n3 4", "3", false);
        testCase(q28, "3 2\n0 1\n1 2", "3", false);
        testCase(q28, "2 2\n0 1\n1 0", "1", true);

        Question q29 = question(
                "Bellman-Ford Shortest Paths",
                "First line: n, e, src. Next e lines: 'u v w', a DIRECTED weighted edge (w may be negative). Print the shortest distance from src to every node 0..n-1, space-separated, using 'INF' for unreachable nodes. If a negative-weight cycle is reachable from src, print exactly NEGATIVE_CYCLE instead.",
                Room.Topic.GRAPHS, Question.Difficulty.HARD,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int src = sc.nextInt();\n        int[][] edges = new int[e][3]; // u, v, weight (directed u -> v, weight may be negative)\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n            edges[i][2] = sc.nextInt();\n        }\n        // TODO: relax all edges n-1 times, then check once more for further relaxation (= negative cycle)\n    }\n}",
                "Input:\n3 3 0\n0 1 4\n1 2 -2\n0 2 3\nOutput:\n0 4 2",
                "1 <= n <= 500"
        );
        testCase(q29, "3 3 0\n0 1 4\n1 2 -2\n0 2 3", "0 4 2", false);
        testCase(q29, "3 3 0\n0 1 1\n1 2 -1\n2 0 -1", "NEGATIVE_CYCLE", false);
        testCase(q29, "2 0 0", "0 INF", true);

        Question q30 = question(
                "Eulerian Circuit Check",
                "First line: n, e. Next e lines: an undirected edge. Print true if the graph has an Eulerian circuit (a closed walk using every edge exactly once) - this requires every vertex with at least one edge to have even degree, and all edges to lie in a single connected component. Print false otherwise.",
                Room.Topic.GRAPHS, Question.Difficulty.MEDIUM,
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int e = sc.nextInt();\n        int[][] edges = new int[e][2];\n        for (int i = 0; i < e; i++) {\n            edges[i][0] = sc.nextInt();\n            edges[i][1] = sc.nextInt();\n        }\n        // TODO: check every non-isolated vertex has even degree, and they're all in one component\n    }\n}",
                "Input:\n3 3\n0 1\n1 2\n2 0\nOutput:\ntrue",
                "1 <= n <= 1000"
        );
        testCase(q30, "3 3\n0 1\n1 2\n2 0", "true", false);
        testCase(q30, "4 3\n0 1\n1 2\n2 3", "false", false);
        testCase(q30, "3 0", "true", true);

    }
}
