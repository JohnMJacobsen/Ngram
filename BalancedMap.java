import java.util.LinkedList;
import java.util.TreeMap;

public class BalancedMap<K extends Comparable<K>, V extends Comparable<V> > implements IMap<K, V> {

    public static void main (String args[]) {
        System.out.println("hi");
    }

    private TreeMap t = new TreeMap<K, V>();

    BalancedMap() {
    }

    BalancedMap(IMap m) {
        for (Object key : m.keyset())
            t.put((K)key, (V)t.get((K)key));
    }

    @Override
    public boolean contains(K key) {
        if (t.containsKey(key))
            return true;
        return false;
    }

    @Override
    public boolean add(K key, V value) {
        if (t.containsKey(key))
            return false;
        return t.put(key, value) != null;

    }

    @Override
    public V delete(K key) {
        return (V) t.remove(key);
    }

    @Override
    public V getValue(K key) {
        return (V) t.get(key);
    }

    @Override
    public K getKey(V value) {
        if (t.containsValue(value)) {
            for (Object key : t.keySet()) {
                if (value.compareTo((V)t.get((K)key)) == 0)
                    return (K) key;
            }
        }
        return null;
    }

    @Override
    public Iterable<K> getKeys(V value) {
        LinkedList<K> keyList = new LinkedList<>();
        for (Object key : t.keySet()) {
            if(((V)t.get((K)key)).compareTo(value) == 0)
                keyList.add((K)key);
        }
        return keyList;
    }

    @Override
    public int size() {
        return t.size();
    }

    @Override
    public boolean isEmpty() {
        return t.isEmpty();
    }

    @Override
    public void clear() {
        if (t.size() > 0)
            t.clear();
    }

    @Override
    public Iterable<K> keyset() {
        return t.keySet();
    }

    @Override
    public Iterable<V> values() {
        return t.values();
    }
}
