package com.gitlab.artismarti.smartsmells

import com.gitlab.artismarti.smartsmells.complexmethod.ComplexMethodDetector
import com.gitlab.artismarti.smartsmells.config.DetectorConfig
import com.gitlab.artismarti.smartsmells.longmethod.LongMethodDetector
import com.gitlab.artismarti.smartsmells.longparam.LongParameterListDetector
import com.gitlab.artismarti.smartsmells.start.DetectorFacade
import spock.lang.Specification

import java.nio.file.Paths
/**
 * @author artur
 */
class DetectorFacadeTest extends Specification {


	def "build facade with three detectors"() {
		expect:
		facade.numberOfDetectors() == 3

		where:
		facade = DetectorFacade.builder()
				.with(new LongParameterListDetector())
				.with(new LongMethodDetector())
				.with(new ComplexMethodDetector())
				.build()
	}

	def "create detector facade from config"() {
		expect:
		facade.numberOfDetectors() == 12

		where:
		path = Paths.get(getClass().getResource("/integration.yml").getFile())
		facade = DetectorFacade.fromConfig(DetectorConfig.load(path))
	}

}
