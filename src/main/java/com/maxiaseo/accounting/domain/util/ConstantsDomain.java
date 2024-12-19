package com.maxiaseo.accounting.domain.util;

import java.util.Set;

public class ConstantsDomain {

    public static enum AbsenceReasonsEnum {
        INC_ARL,        //INCAPACIDAD_ARL
        INC,            //INCAPACIDAD_CON_SOPORTE,
        INC_SIN_SOPR,   //INCAPACIDAD_SIN_SOPORTE,
        PNR,            //PERMISO_NO_REMUNERADO,
        LR,             //LICENCIA_REMUNERADA,
        AUS,            //AUSENTISMO,
        EPS,            //COLABORADOR_EN_EPS,
        RET,            //RETIRO
        DESC            //DESCANSO
    }

    public static final Set<String> VALID_CODES = Set.of(
            AbsenceReasonsEnum.INC_ARL.toString(),
            AbsenceReasonsEnum.INC.toString(),
            AbsenceReasonsEnum.INC_SIN_SOPR.toString(),
            AbsenceReasonsEnum.PNR.toString(),
            AbsenceReasonsEnum.LR.toString(),
            AbsenceReasonsEnum.AUS.toString(),
            AbsenceReasonsEnum.EPS.toString(),
            AbsenceReasonsEnum.RET.toString(),
            AbsenceReasonsEnum.DESC.toString()
            );

    public static final Integer FIRST_DAY_OF_FIRST_FORTNIGHT = 1;
    public static final Integer FIRST_DAY_OF_SECOND_FORTNIGHT = 16;

    public static final Integer FIRST_ROW_WITH_VALID_DATA_INDEX = 1;
    public static final Integer FIRST_COLUM_WITH_VALID_DATA_INDEX = 2;

    public static final Integer EMPLOYEE_DOCUMENT_ID_INDEX = 0;
    public static final Integer EMPLOYEE_NAME_INDEX = 1;

    public static final String VALUES_OUT_OF_DATE_RANGE_MESSAGE = "El ultimo dia de %s es %s pero esta 'al final'";
}
