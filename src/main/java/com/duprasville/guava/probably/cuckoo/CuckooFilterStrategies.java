/*
 * Copyright (C) 2015 Brian Dupras
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.duprasville.guava.probably.cuckoo;

import com.google.common.hash.Funnel;

import com.duprasville.guava.probably.CuckooFilter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.mod;

/**
 * Collections of strategies of generating the f-bit fingerprint, index i1 and index i2 required for
 * an element to be mapped to a CuckooTable of m buckets with hash function h. These strategies are
 * part of the serialized form of the Cuckoo filters that use them, thus they must be preserved as
 * is (no updates allowed, only introduction of new versions). <p/> Important: the order of the
 * constants cannot change, and they cannot be deleted - we depend on their ordinal for CuckooFilter
 * serialization.
 *
 * @author Alex Beal
 */
public enum CuckooFilterStrategies {
  RESERVED() {
    @Override
    public Strategy strategy() {
      return new AbstractCuckooStrategy(this.ordinal()) {
        @Override
        public long index(int hash, long m) {
          return 0;
        }

        @Override
        public long altIndex(long index, int fingerprint, long m) {
          return 0;
        }

        @Override
        protected int pickEntryToKick(int numEntriesPerBucket) {
          return 0;
        }

        @Override
        protected long maxRelocationAttempts() {
          return 0;
        }

        public <T> boolean add(T object, Funnel<? super T> funnel, CuckooTable table) {
          return true;
        }

        public <T> boolean remove(T object, Funnel<? super T> funnel, CuckooTable table) {
          return true;
        }

        public <T> boolean contains(T object, Funnel<? super T> funnel, CuckooTable table) {
          return true;
        }
      };
    }
  },
  
  MURMUR128_BEALDUPRAS_32() {
    @Override
    public Strategy strategy() {
      return new CuckooMurmurBealDupras32Strategy(this.ordinal());
    }
  };

  public abstract Strategy strategy();
}

