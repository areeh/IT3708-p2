package logic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SupportAlgs {

	public SupportAlgs() {
		// TODO Auto-generated constructor stub
	}
	
    public static BigDecimal scale(final BigDecimal valueIn, final BigDecimal baseMin, final BigDecimal baseMax, final BigDecimal limitMin, final BigDecimal limitMax) {
        return ((limitMax.subtract(limitMin)).multiply((valueIn.subtract(baseMin)).divide((baseMax.subtract(baseMin)), 10, RoundingMode.HALF_UP).add(limitMin)));
    }
    
    public static double scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin, final double limitMax) {
    	if ((baseMax - baseMin) <= 0.00001) {
    		return 1;
    	}
    	double res = (limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin) + limitMin;
    	if (Double.isNaN(res)) {
    		System.err.println("Scale returned NaN");
    	} else if (Double.isInfinite(res)) {
    		return 1;
    	}
        return res;
    }

}
