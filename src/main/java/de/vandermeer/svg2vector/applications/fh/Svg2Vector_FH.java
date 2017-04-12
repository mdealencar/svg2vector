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

package de.vandermeer.svg2vector.applications.fh;

import java.awt.Color;
import java.io.File;
import java.util.Map.Entry;

import de.vandermeer.svg2vector.applications.base.AppBase;
import de.vandermeer.svg2vector.applications.base.AppProperties;
import de.vandermeer.svg2vector.applications.base.SvgTargets;
import de.vandermeer.svg2vector.applications.is.converters.FhConverter;

/**
 * The Svg2Vector application using the FreeHep library.
 * It an SVG graphic to a vector format.
 * Currently supported are EMF, PDF and SVG.
 * The tool does support SVG and SVGZ input formats from file or URI.
 * It also can deal with SVG layers.
 * All options can be set via command line.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class Svg2Vector_FH extends AppBase<BatikLoader, AppProperties<BatikLoader>> {

	/** Application name. */
	public final static String APP_NAME = "s2v-hp";

	/** Application display name. */
	public final static String APP_DISPLAY_NAME = "Svg2Vector FreeHep";

	/** Application version, should be same as the version in the class JavaDoc. */
	public final static String APP_VERSION = "v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8";

	/** Application option for not-transparent mode. */
	AO_NotTransparent optionNotTransparent = new AO_NotTransparent(false, 'n', "switch off transparency");

	/** Application option for clip mode. */
	AO_Clip optionClip = new AO_Clip(false, 'c', "activate clip property");

	/** Application option for background-color mode. */
	AO_BackgroundColor optionBackgroundColor = new AO_BackgroundColor(false, 'r', "sets the background color");

	/** Application option for no-background mode. */
	AO_NoBackground optionNoBackground = new AO_NoBackground(false, 'b', "switch off background property");

	/**
	 * Returns a new application.
	 */
	public Svg2Vector_FH(){
		super(new AppProperties<BatikLoader>(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf, SvgTargets.svg}, new BatikLoader()));

		this.addOption(this.optionNotTransparent);
		this.addOption(this.optionClip);
		this.addOption(this.optionBackgroundColor);
		this.addOption(this.optionNoBackground);
	}

	@Override
	public int executeApplication(String[] args) {
		// parse command line, exit with help screen if error
		int ret = super.executeApplication(args);
		if(ret!=0){
			return ret;
		}

		SvgTargets target = this.getProps().getTarget();

		FhConverter converter = target.getConverter();
		if(converter==null){
			this.printErrorMessage("no converter found for target <" + target.name() + ">");
			return -20;
		}

		converter.setPropertyTransparent(!this.optionNotTransparent.inCli());
		converter.setPropertyClip(this.optionClip.inCli());
		converter.setPropertyBackground(!this.optionNoBackground.inCli());
		converter.setPropertyTextAsShapes(this.getProps().doesTextAsShape());
		if(this.optionBackgroundColor.inCli()){
			Color color = Color.getColor(this.optionBackgroundColor.getValue());
			converter.setPropertyBackgroundColor(color);
		}

		String err;
		BatikLoader loader = this.getProps().getLoader();
		if(this.getProps().doesLayers()){
			for(Entry<String, Integer> entry : loader.getLayers().entrySet()){
				loader.switchOffAllLayers();
				loader.switchOnLayer(entry.getKey());
				err = converter.convertDocument(loader, new File(this.getProps().getFnOut(entry) + "." + target.name()));
				if(err!=null){
					this.printErrorMessage(err);
					return -99;//TODO
				}
			}
		}
		else{
			err = converter.convertDocument(loader, this.getProps().getFoutFile());
			if(err!=null){
				this.printErrorMessage(err);
				return -99;//TODO
			}
		}

		return 0;
	}

//		if(this.optionVerbose.inCli()==true){
//			UserProperties up = properties.getProperties();
//			Set<Object> keys = up.keySet();
//			Iterator<Object>it = keys.iterator();
//			while(it.hasNext()){
//				String key = it.next().toString();
//				String val = up.getProperty(key);
//				key=key.substring(key.lastIndexOf('.')+1, key.length());
//				this.printProgress("SVG property <" + key + ">=" + val);
//			}
//		}

	@Override
	public String getAppName() {
		return APP_NAME;
	}

	@Override
	public String getAppDisplayName(){
		return APP_DISPLAY_NAME;
	}

	@Override
	public String getAppDescription() {
		return "Converts SVG graphics into other vector formats using FreeHep libraries, with options for handling layers";
	}

	@Override
	public String getAppVersion() {
		return APP_VERSION;
	}
}
