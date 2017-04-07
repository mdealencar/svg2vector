/* Copyright 2017 Sven van der Meer <vdmeer.sven@mykolab.com>
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
 */

package de.vandermeer.svg2vector.applications.options;

import java.net.URI;

import org.apache.commons.cli.Option;

import de.vandermeer.execs.options.AbstractApplicationOption;

/**
 * Application option "uri".
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.1.0 build 170405 (05-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class AO_UriIn extends AbstractApplicationOption<String> {

	/** The input URI, set when parsing command line. */
	protected URI uri;

	/**
	 * Returns the new option.
	 * @param required true if option is required, false of it is optional
	 * @param longDescription option long description
	 * @throws NullPointerException - if description parameter is null
	 * @throws IllegalArgumentException - if description parameter is empty
	 */
	public AO_UriIn(boolean required, String longDescription){
		this(required, null, longDescription);
	}

	/**
	 * Returns the new option.
	 * @param required true if option is required, false of it is optional
	 * @param shortOption character for sort version of the option
	 * @param longDescription option long description
	 * @throws NullPointerException - if description parameter is null
	 * @throws IllegalArgumentException - if description parameter is empty
	 */
	public AO_UriIn(boolean required, Character shortOption, String longDescription){
		super("input URI", longDescription);

		Option.Builder builder = (shortOption==null)?Option.builder():Option.builder(shortOption.toString());
		builder.longOpt("uri");
		builder.hasArg().argName("URI");
		builder.required(required);
		this.setCliOption(builder.build());
	}

	@Override
	public String convertValue(Object value) {
		if(value==null){
			return null;
		}
		return value.toString();
	}

	/**
	 * Sets the URI.
	 * @param uri new URI, ignored if null
	 */
	public void setURI(URI uri){
		if(uri!=null){
			this.uri = uri;
		}
	}

	/**
	 * Returns the URI.
	 * @return URI, null if not set
	 */
	public URI getURI(){
		return this.uri;
	}
}
