package org.entirej.framework.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EJDataMap<K, V>
{

    private List<Node> data = new ArrayList<>();

    private class Node
    {
        K k;
        V v;
    }

    public Collection<V> values()
    {
        return data.stream().map(n -> n.v).collect(Collectors.toList());
    }

    public boolean containsKey(K key)
    {
        return data.stream().anyMatch(n -> Objects.equals(n.k, key));
    }

    public void put(K k, V v)
    {

        EJDataMap<K, V>.Node node = data.stream().filter(n -> Objects.equals(n.k, k)).
                findFirst().orElseGet(() -> {
                                                Node n = new Node();
                                                n.k = k;
                                                data.add(n);
                                                return n;
                                            });
        node.k = k;
        node.v = v;
    }

    public V get(K k)
    {
        return data.stream().filter(n -> Objects.equals(n.k, k)).findFirst().map(n -> n.v).orElseGet(() -> null);
    }

    public Collection<K> keySet()
    {
        return data.stream().map(n -> n.k).collect(Collectors.toList());
    }
}
