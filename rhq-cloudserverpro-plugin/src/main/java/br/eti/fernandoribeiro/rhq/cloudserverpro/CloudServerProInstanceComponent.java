/*
 * Copyright 2013 Fernando Ribeiro
 * 
 * This file is part of Cloud Server Pro Tools.
 *
 * Cloud Server Pro Tools is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Cloud Server Pro Tools is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Cloud Server Pro Tools. If not, see <http://www.gnu.org/licenses/>.
 */
package br.eti.fernandoribeiro.rhq.cloudserverpro;

import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.pluginapi.inventory.ResourceComponent;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.platform.PlatformComponent;

public final class CloudServerProInstanceComponent implements
		ResourceComponent<PlatformComponent>, OperationFacet {

	@Override
	public AvailabilityType getAvailability() {
		return AvailabilityType.UNKNOWN;
	}

	@Override
	public OperationResult invokeOperation(final String name,
			final Configuration parameters) {

		if (!("reboot".equals(name)))
			throw new UnsupportedOperationException("The operation " + name
					+ " is not supported");

		return null;
	}

	@Override
	public void start(final ResourceContext<PlatformComponent> context) {
	}

	@Override
	public void stop() {
	}

}