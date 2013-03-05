package de.lmu.ifi.dbs.elki.persistent;

/*
 This file is part of ELKI:
 Environment for Developing KDD-Applications Supported by Index-Structures

 Copyright (C) 2013
 Ludwig-Maximilians-Universität München
 Lehr- und Forschungseinheit für Datenbanksysteme
 ELKI Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Page file factory for memory page files.
 * 
 * @author Erich Schubert
 * 
 * @param <P> Page type
 */
public class MemoryPageFileFactory<P extends Page> extends AbstractPageFileFactory<P> {
  /**
   * Constructor.
   */
  public MemoryPageFileFactory(int pageSize, long cacheSize) {
    super(pageSize, cacheSize);
  }

  @Override
  public PageFile<P> newPageFile(Class<P> cls) {
    return new MemoryPageFile<>(pageSize);
  }
  
  /**
   * Parameterization class.
   * 
   * @author Erich Schubert
   */
  public static class Parameterizer extends AbstractPageFileFactory.Parameterizer<Page> {
    @Override
    protected MemoryPageFileFactory<Page> makeInstance() {
      return new MemoryPageFileFactory<>(pageSize, cacheSize);
    }    
  }
}