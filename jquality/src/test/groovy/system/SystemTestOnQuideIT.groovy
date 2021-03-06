package system

import io.gitlab.arturbosch.jpal.core.JPAL
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.api.MetricFacade
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.metrics.ClassInfo
import io.gitlab.arturbosch.smartsmells.out.XMLWriter
import spock.lang.Specification

import java.nio.file.Paths
import java.util.stream.Collectors

/**
 * @author artur
 */
class SystemTestOnQuideIT extends Specification {

	def "compilation storage"() {
		given:
//		def path = "/home/artur/Repos/quide-master/Implementierung/QuideService/src/main"
//		def path = "/home/artur/Repos/quide-master/Implementierung/QuIDE_Plugin/"
		def path = "/home/artur/Arbeit/tools/ismell/src/main"
//		def path = "/home/artur/Repos/elasticsearch/core/src/main/"
//		def path = "/home/artur/Repos/RxJava/src/main"
//		def path = "/home/artur/Arbeit/agst/pooka-co/trunk/pooka/src"

		when:
		def storage = JPAL.newInstance(Paths.get(path))
		def types = storage.getAllQualifiedTypes()

		then:
		types.each { println it }
		println "size: ${types.size()}"
	}

	def "metrics on quide"() {
		given:
//		def path = "/home/artur/Repos/quide-master/Implementierung/QuideService/src/main"
//		def path = "/home/artur/Arbeit/tools/ismell/src/main"
		def path = "/home/artur/Repos/elasticsearch/core/src/main/" // 26-28s
//		def path = "/home/artur/Repos/RxJava/src/main"
//		def path = "/home/artur/Arbeit/agst/pooka-co/trunk/pooka/src"
//		def path = "/home/artur/Repos/vert.x/src/main/java"
//		def path = "/home/artur/Repos/netty"

		when:
		def result = new MetricFacade().run(Paths.get(path))
		result.each { println(it.toString()) }
		println "size: ${result.size()}"

		def locSum = result.stream().mapToInt { (it as ClassInfo).sloc }.sum()
		def locCount = result.stream().mapToInt { (it as ClassInfo).sloc }.count()
		println "project sloc: $locSum"
		println "project sloc per class: ${locSum / locCount}"

		def wmcSum = result.stream().mapToInt { (it as ClassInfo).wmc }.sum()
		def wmcCount = result.stream().mapToInt { (it as ClassInfo).wmc }.count()
		println "project wmc: ${wmcSum / wmcCount}"

		def aftdSum = result.stream().mapToInt { (it as ClassInfo).atfd }.sum()
		def aftdCount = result.stream().mapToInt { (it as ClassInfo).atfd }.count()
		println "project atfd: ${aftdSum / aftdCount}"

		def tccSum = result.stream().mapToDouble() { (it as ClassInfo).tcc }.sum()
		def tccCount = result.stream().mapToDouble { (it as ClassInfo).tcc }.count()
		println "project tcc: ${tccSum / tccCount}"

		def nomSum = result.stream().mapToInt { (it as ClassInfo).nom }.sum()
		def nomCount = result.stream().mapToInt { (it as ClassInfo).nom }.count()
		println "project nom: ${nomSum / nomCount}"

		def noaSum = result.stream().mapToInt { (it as ClassInfo).noa }.sum()
		def noaCount = result.stream().mapToInt { (it as ClassInfo).noa }.count()
		println "project noa: ${noaSum / noaCount}"

		def amlSum = result.stream().mapToDouble() { (it as ClassInfo).mlm }.sum()
		def amlCount = result.stream().mapToDouble() { (it as ClassInfo).mlm }.count()
		println "project mlm: ${amlSum / amlCount}"

		def aplSum = result.stream().mapToDouble { (it as ClassInfo).plm }.sum()
		def aplCount = result.stream().mapToDouble { (it as ClassInfo).plm }.count()
		println "project plm: ${aplSum / aplCount}"

		def smlSum = result.stream().mapToDouble { (it as ClassInfo).mld }.sum()
		def smlCount = result.stream().mapToDouble { (it as ClassInfo).mld }.count()
		println "project mld: ${smlSum / smlCount}"

		def splSum = result.stream().mapToDouble { (it as ClassInfo).pld }.sum()
		def splCount = result.stream().mapToDouble { (it as ClassInfo).pld }.count()
		println "project pld: ${splSum / splCount}"

		then:
		result.size() > 0
	}

	def "run on quide and find no same feature envy twice"() {
		given:
		def path = "/home/artur/Repos/quide-master/Implementierung/QuideService/src/main"
//		def path = "/home/artur/Arbeit/tools/ismell/src/main"
//		def path = "/home/artur/Arbeit/agst/pooka-co/trunk/pooka/src"
//		def path = "/home/artur/Repos/elasticsearch/core/src/main/" // 32-32 s
//		def path = Paths.getResource("/cornercases").getFile()
		def result = DetectorFacade.builder().fullStackFacade().run(Paths.get(path))
		def envies = result.of(Smell.FEATURE_ENVY)
		when:
		def duplicates = envies.stream()
				.filter { Collections.frequency(envies, it) > 1 }
				.collect(Collectors.toSet())
//		envies.each { println it.toString() }
		result.of(Smell.GOD_CLASS).each { println it.toString() }
//		println result.of(Smell.CYCLE).size()
		then:
		duplicates.isEmpty()
		when:
		def xml = XMLWriter.toXml(result)
//		println xml
		then:
		xml.contains("FeatureEnvy")
	}
}
