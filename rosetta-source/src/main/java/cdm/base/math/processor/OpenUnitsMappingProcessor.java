package cdm.base.math.processor;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;

@SuppressWarnings("unused")
public class OpenUnitsMappingProcessor extends MappingProcessor {

	public OpenUnitsMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path openUnitPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		UnitType.UnitTypeBuilder unitTypeBuilder = (UnitType.UnitTypeBuilder) parent;
		// for single underliers
		setPerUnitOf(unitTypeBuilder, getSingleUnderlierPath(openUnitPath, "equity"), FinancialUnitEnum.SHARE);
		setPerUnitOf(unitTypeBuilder, getSingleUnderlierPath(openUnitPath, "bond"), FinancialUnitEnum.SHARE);
		setPerUnitOf(unitTypeBuilder, getSingleUnderlierPath(openUnitPath, "index"), FinancialUnitEnum.INDEX_UNIT);
		setPerUnitOf(unitTypeBuilder, getSingleUnderlierPath(openUnitPath, "commodity"), FinancialUnitEnum.CONTRACT);
		// for basketConstituent
		setPerUnitOf(unitTypeBuilder, getBasketConstituentPath(openUnitPath, "equity"), FinancialUnitEnum.SHARE);
		setPerUnitOf(unitTypeBuilder, getBasketConstituentPath(openUnitPath, "bond"), FinancialUnitEnum.SHARE);
		setPerUnitOf(unitTypeBuilder, getBasketConstituentPath(openUnitPath, "index"), FinancialUnitEnum.INDEX_UNIT);
		setPerUnitOf(unitTypeBuilder, getBasketConstituentPath(openUnitPath, "commodity"), FinancialUnitEnum.CONTRACT);
	}

	private Path getSingleUnderlierPath(Path openUnitPath, String lastElement) {
		// openUnits path:
		// /underlyer/singleUnderlyer/openUnits
		// underlier type:
		// /underlyer/singleUnderlyer/equity|bond|index
		return openUnitPath.getParent().addElement(lastElement);
	}

	private Path getBasketConstituentPath(Path openUnitPath, String lastElement) {
		// openUnits path:
		// /underlyer/basket/basketConstituent/constituentWeight/openUnits
		// underlier type:
		// /underlyer/basket/basketConstituent/equity|bond|index
		return openUnitPath.getParent().getParent().addElement(lastElement);
	}

	private void setPerUnitOf(UnitType.UnitTypeBuilder unitTypeBuilder, Path synonymPath, FinancialUnitEnum financialUnit) {
		getNonNullMappedValue(getMappings(), synonymPath)
				.ifPresent(x -> unitTypeBuilder.setFinancialUnit(financialUnit));
	}
}
