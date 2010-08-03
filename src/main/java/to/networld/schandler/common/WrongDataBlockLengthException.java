/**
 * SmartCard Handler Library
 *
 * Copyright (C) 2010 by Networld Project
 * Written by Alex Oberhauser <oberhauseralex@networld.to>
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */

package to.networld.schandler.common;

/**
 * Exception that indicates that the block size is wrong. This exception could
 * occur if the caller tries to write to match or to few data to a block.
 * 
 * @author Alex Oberhauser
 *
 */
public class WrongDataBlockLengthException extends Exception {
	private static final long serialVersionUID = 2380357670529684015L;

	public WrongDataBlockLengthException(String _exceptionMessage) {
		super(_exceptionMessage);
	}
}
