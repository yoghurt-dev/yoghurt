package ${modulePackage}.ao;

import io.swagger.annotations.ApiModel;
import ${modulePackage}.po.${className}PO;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;


/**
 * @author ${userName}
 */
@ApiModel(value = "${tableComment}")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class ${className}AO extends ${className}PO{

}
