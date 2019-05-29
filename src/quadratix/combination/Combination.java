package quadratix.combination;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.NumberOperations;
import quadratix.bits.Bits;
import quadratix.stats.Randomizable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class that represents a combination of number.
 */
public class Combination extends Vector<Long> implements Randomizable<Combination>, Serializable, Cloneable {

    public Combination(@NotNull Long... elements) {
        super(elements.length);
        this.addAll(Arrays.asList(elements));
    }

    public Combination(@NotNull Integer... elements) {
        super(elements.length);
        this.addAll(Arrays.stream(elements).map(Long::new).collect(Collectors.toList()));
    }

    public Combination(int length) {
        super(length);
    }

    public Combination() {
        super(0);
    }

    public Combination(@NotNull Collection<? extends Long> collection) {
        super(collection);
    }

    /**
     * Swap two element at indexes `i` and `j`.
     *
     * @param i Index of the first element to swap.
     * @param j Index of the second element to swap.
     */
    public void swap(int i, int j) {
        Long li = get(i);
        set(i, get(j));
        set(j, li);
    }

    /**
     * Swap two element at index `i` and a random index `j`.
     *
     * @param i Index of the first element to swap.
     */
    public void swap(int i) {
        if (size() > 1) {
            // Make a list of available indexes (all indexes but i)
            ArrayList<Integer> indexes = new ArrayList<>(size() - 1);
            for (int j = 0; j < size(); j++)
                if (j != i)
                    indexes.add(j);

            int j = indexes.get(new Random().nextInt(indexes.size()));
            swap(i, j);
        }
    }

    /**
     * Swap two elements randomly.
     */
    public void swap() {
        if (size() > 1) {
            // Make a list of available indexes (all indexes but i)
            ArrayList<Integer> indexes = new ArrayList<>(size());
            for (int i = 0; i < size(); i++)
                indexes.add(i);

            int index = new Random().nextInt(indexes.size());
            int i = indexes.get(index);
            indexes.remove(index);
            index = new Random().nextInt(indexes.size());
            int j = indexes.get(index);
            swap(i, j);
        }
    }

    @NotNull
    @Contract(pure = true)
    public static ArrayList<Combination> generateAllPossibility(final int length) {
        ArrayList<Combination> possibilities = new ArrayList<>();

        // Compute the "normal" combination (1, 2, 3, 4, ... n).
        Combination normal = new Combination(length);
        for (int i = 0; i < length; i++) {
            normal.add((long) i + 1);
        }

        possibilities.add(normal);

        // Generate all bits
        for (int i = 0; i <= length - 1; i++) {
            for (int j = i + 1; j < length; j++) {
                Combination c = new Combination(normal);
                c.swap(i, j);
                possibilities.add(c);
            }
        }

        return possibilities;
    }

    @NotNull
    @Contract(pure = true)
    public static Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> generateAllNeighbors() {
        return generateAllNeighbors(null);
    }

    @NotNull
    @Contract(pure = true)
    public static Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> generateAllNeighbors(@Nullable Integer swapDistance) {
        return combination -> {
            HashMap<Combination, ElementaryFunction<Combination>> map = new HashMap<>();

            for (int i = 0; i < combination.size() - 1; i++) {
                for (int j = i + 1; j < combination.size(); j++) {
                    Combination c = new Combination(combination);
                    if (swapDistance == null || Math.abs(i - j) <= swapDistance) {
                        c.swap(i, j);
                        final int final_i = i, final_j = j;
                        map.put(c, new ElementaryFunction<Combination>() {
                            @Override
                            public Combination apply(Combination bits) {
                                c.swap(final_i, final_j);
                                return bits;
                            }

                            @Override
                            public @NotNull Function<Combination, Combination> invert() {
                                return bits1 -> {
                                    c.swap(final_i, final_j);
                                    return bits1;
                                };
                            }
                        });
                    }
                }
            }

            return map;
        };
    }

    @NotNull
    @Contract(pure = true)
    public static Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> generateRandomNeighborhood(Integer neighSize) {
        return combination -> {
            Random random = new Random();
            HashMap<Combination, ElementaryFunction<Combination>> map = new HashMap<>();

            for (int i = 0; i < neighSize; i++) {
                Combination c = new Combination(combination);

                int a = random.nextInt(combination.size());
                int b = random.nextInt(combination.size());

                c.swap(a, b);
                final int final_a = a, final_b = b;
                map.put(c, new ElementaryFunction<Combination>() {
                    @Override
                    public Combination apply(Combination bits) {
                        c.swap(final_a, final_b);
                        return bits;
                    }

                    @Override
                    public @NotNull Function<Combination, Combination> invert() {
                        return bits1 -> {
                            c.swap(final_a, final_b);
                            return bits1;
                        };
                    }
                });
            }

            return map;
        };
    }


    //region RANDOMIZABLE OVERRIDE

    @Override
    public Combination generateRandom() {
        return null;
    }

    public static Combination generateRandom(final int length) {
        Combination c = new Combination(length);
        for (int i = 0; i < length; i++) c.add((long) i + 1);
        for (int i = 0; i < length; i++) c.swap();
        return c;
    }

    //endregion

    //region LIST OVERRIDES

    @NotNull
    @Contract(value = " -> new", pure = true)
    public static NumberOperations<Combination> getOperations() {
        return new NumberOperations<Combination>() {
            @Nullable
            @Override
            public Combination plus(@Nullable Combination t1, @Nullable Combination t2) {
                throw new NotImplementedException();
            }

            @Nullable
            @Override
            public Combination minus(@Nullable Combination t1, @Nullable Combination t2) {
                throw new NotImplementedException();
            }

            @Nullable
            @Override
            public Combination multiply(@Nullable Combination t1, @Nullable Combination t2) {
                throw new NotImplementedException();
            }

            @Nullable
            @Override
            public Combination divide(@Nullable Combination t1, @Nullable Combination t2) {
                throw new NotImplementedException();
            }

            @NotNull
            @Override
            public Combination getZero() {
                return new Combination();
            }

            @Override
            public int compare(Combination o1, Combination o2) {
                return 0;
            }
        };
    }

    //endregion

    //region GETTER & SETTER


    //endregion

    //region OBJECT OVERRIDE

    @Override
    public synchronized String toString() {
        return String.join(", ", () -> new Iterator<CharSequence>() {

            private int i;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public CharSequence next() {
                return Long.toString(get(i++));
            }
        });
    }

    //endregion
}
