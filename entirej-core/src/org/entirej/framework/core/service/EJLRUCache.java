/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An LRU cache, based on <code>LinkedHashMap</code>.
 * 
 * <p>
 * This cache has a fixed maximum number of elements (<code>cacheSize</code>).
 * If the cache is full and another entry is added, the LRU (least recently
 * used) entry is dropped.
 * 
 * <p>
 * This class is thread-safe. All methods of this class are synchronized.
 */
public class EJLRUCache<K, V> extends LinkedHashMap<K, V> implements Serializable
{
    public static final int   DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR      = 0.75f;
    
    private int               _cacheSize;
    
    private long              _missed                  = 0;
    private long              _hit                     = 0;
    private boolean           _trackEfficiency         = true;
    
    /**
     * Creates a new LRU cache.
     * 
     * @param cacheSize
     *            the maximum number of entries that will be kept in this cache.
     */
    public EJLRUCache(int cacheSize)
    {
        // create the LinkedHashMap with access-order set to true
        super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
        
        _cacheSize = cacheSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
    {
        return size() > _cacheSize;
    }
    
    /**
     * Retrieves an entry from the cache.<br>
     * The retrieved entry becomes the MRU (most recently used) entry.
     * 
     * @param key
     *            the key whose associated value is to be returned.
     * @return the value associated to this key, or null if no value with this
     *         key exists in the cache.
     */
    public synchronized V getEntry(K key)
    {
        V entry = get(key);
        if (_trackEfficiency && entry != null)
        {
            _hit++;
        }
        return entry;
    }
    
    /**
     * Adds an entry to this cache. The new entry becomes the MRU (most recently
     * used) entry. If an entry with the specified key already exists in the
     * cache, it is replaced by the new entry. If the cache is full, the LRU
     * (least recently used) entry is removed from the cache.
     * 
     * @param key
     *            the key with which the specified value is to be associated.
     * @param value
     *            a value to be associated with the specified key.
     */
    public synchronized void putEntry(K key, V value)
    {
        if (_trackEfficiency)
        {
            _missed++;
        }
        put(key, value);
    }
    
    /**
     * Clears the cache.
     */
    @Override
    public synchronized void clear()
    {
        super.clear();
    }
    
    /**
     * Returns the number of used entries in the cache.
     * 
     * @return the number of entries currently in the cache.
     */
    public synchronized int getEntriesNumber()
    {
        return size();
    }
    
    /**
     * Returns a <code>Collection</code> that contains a copy of all cache
     * entries.
     * 
     * @return a <code>Collection</code> with a copy of the cache content.
     */
    public synchronized Collection<Map.Entry<K, V>> getAll()
    {
        return new ArrayList<Map.Entry<K, V>>(entrySet());
    }
    
    /**
     * Returns the cached size
     * 
     * @return the size of this cache
     */
    public int getCacheSize()
    {
        return _cacheSize;
    }
    
    /**
     * Sets the size of this cache
     * 
     * @param _cacheSize
     *            - The new chase size
     */
    public void setCacheSize(int cacheSize)
    {
        _cacheSize = cacheSize;
    }
    
    /**
     * Returns the number of the missed entries (values that were not found in
     * the cache)<br>
     * This method is relevant only if track efficiency is turned on.
     * 
     * @return the number of missed entries
     * @see #setTrackEfficiency(boolean)
     * @see #isTrackEfficiency()
     */
    public long getMissedEntries()
    {
        return _missed;
    }
    
    /**
     * Returns the number of the hit entries (values that were found in the
     * cache)<br>
     * This method is relevant only if track efficiency is turned on.
     * 
     * @return the number of hit entries
     * @see #setTrackEfficiency(boolean)
     * @see #isTrackEfficiency()
     */
    public long getHitEntries()
    {
        return _hit;
    }
    
    /**
     * Checks if the track efficiency is turned on.
     * 
     * @return true id the track efficiency is on, otherwise false
     */
    public boolean isTrackEfficiency()
    {
        return _trackEfficiency;
    }
    
    /**
     * Enables/disables the track efficiency.
     * 
     * @param trackEfficiency
     *            true for enabling and false for stopping
     */
    public void setTrackEfficiency(boolean trackEfficiency)
    {
        _trackEfficiency = trackEfficiency;
    }
}
