package ru.nsu.sidey383.lab1.model.file.base;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.*;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class LinkDUFile extends BaseDUFile implements ReferenceDUFile {

    Collection<DUFile> singletonCollection = new SingletonCollection<>();

    private final Path reference;

    public LinkDUFile(long size, Path path, Path resolved) {
        super(size, path);
        this.reference = resolved;
    }

    @Override
    public DUFileType getFileType() {
        return DUFileType.LINK;
    }

    @Override
    public Collection<DUFile> getChildren() {
        return singletonCollection;
    }

    @Override
    public void addChild(DUFile file) {
        singletonCollection.add(file);
    }

    @Override
    public Path getReference() {
        return reference;
    }

    @Override
    public void freeChild() {
        singletonCollection.clear();
    }

    private static class SingletonCollection<T> implements Collection<T> {

        private T value;

        private boolean isEmpty;

        public SingletonCollection() {
            value = null;
            isEmpty = true;
        }

        @Override
        public int size() {
            return isEmpty ? 0 : 1;
        }

        @Override
        public boolean isEmpty() {
            return isEmpty;
        }

        @Override
        public boolean contains(Object o) {
            return !isEmpty && Objects.equals(value, o);
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return new Iterator<>() {
                private boolean hasNext = true;
                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public T next() {
                    if (hasNext) {
                        hasNext = false;
                        return value;
                    }
                    return null;
                }

                @Override
                public void remove() {
                    if (!hasNext)
                        SingletonCollection.this.remove(value);
                }
            };
        }

        @Override
        public Object [] toArray() {
            return isEmpty ? new Object[0] : new Object[] {value};
        }

        @Override
        public <T1> T1 [] toArray(@NotNull T1[] a) {
            if (!isEmpty) {
                if (a.length == 0)
                    a = (T1[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), 1);
                a[0] = (T1) value;
            }
            return a;
        }

        @Override
        public boolean add(T t) {
            if (!isEmpty)
                return false;
            value = t;
            isEmpty = false;
            return true;
        }

        @Override
        public boolean remove(Object o) {
            if (!isEmpty && Objects.equals(value, o)) {
                value = null;
                isEmpty = true;
                return true;
            }
            return false;
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            if (isEmpty)
                return c.size() == 0;
            return c.stream().allMatch(v -> Objects.equals(v, value));
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends T> c) {
            Iterator<? extends T> iterator = c.iterator();
            if (!iterator.hasNext())
                return true;
            if (isEmpty)
                return false;
            value = iterator.next();
            isEmpty = false;
            return iterator.hasNext();
        }

        @Override
        public boolean removeAll(@NotNull Collection<?> c) {
            if(!isEmpty && c.stream().anyMatch(v -> Objects.equals(v, value))) {
                value = null;
                isEmpty = true;
                return true;
            }
            return false;
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {
            value = null;
            isEmpty = true;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof SingletonCollection<?> sc &&
                    sc.isEmpty == isEmpty &&
                    (
                            (isEmpty) ||
                                    Objects.equals(sc.value, value)
                    ) ;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }
    }
}
