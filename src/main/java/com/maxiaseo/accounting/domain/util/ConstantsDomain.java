package com.maxiaseo.accounting.domain.util;

import java.time.LocalTime;
import java.util.Set;

public class ConstantsDomain {

    public static final LocalTime NIGHT_START = LocalTime.of(21, 0);
    public static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    public static final Long MAX_HOURS_BY_DAY = 8L;
    public static final Long MAX_HOURS_BY_WEEK = 48L;

    public static final Integer FIRST_DAY_OF_MONTH = 1;
    public static final Integer LAST_DAY_OF_FIRST_FORTNIGHT = 15;
    public static final Long FIRST_HOUR_WORKED = 1L;

    public static final String SIIGO_FORMAT_NAME = "Subir novedades desde Excel.xlsx";

    public enum OvertimeSurchargeTypeEnum {
        HOLIDAY, NIGHT_HOLIDAY
    }

    public enum OvertimeTypeEnum {
        DAY, NIGHT, HOLIDAY, NIGHT_HOLIDAY
    }

    public enum SurchargeTypeEnum {
        NIGHT,
        HOLIDAY,
        NIGHT_HOLIDAY,
        DAY
    }

    public enum AbsenceReasonsEnum {
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

    public static final String SURCHARGE_NIGHT_SIIGO_NEW = "06- Hora nocturna - Ingreso";
    public static final String SURCHARGE_HOLIDAY_SIIGO_NEW = "08- Hora dominical o festiva - Ingreso";
    public static final String SURCHARGE_NIGHT_HOLIDAY_SIIGO_NEW = "10- Hora nocturna festiva - Ingreso";

    public static final String OVERTIME_DAY_SIIGO_NEW = "10- Horas extras diurnas 125%- Ingreso";
    public static final String OVERTIME_NIGHT_SIIGO_NEW = "12- Horas extras nocturnas 175%- Ingreso";
    public static final String OVERTIME_HOLIDAY_SIIGO_NEW = "14- Horas extras festivas 200%- Ingreso";
    public static final String OVERTIME_NIGHT_HOLIDAY_SIIGO_NEW = "16- Horas extras nocturnas festivas 250%- Ingreso";

    public static final String SURCHARGE_OVERTIME_HOLIDAY_SIIGO_NEW = "20- Recargo extra festivo - Ingreso";
    public static final String SURCHARGE_OVERTIME_NIGHT_HOLIDAY_SIIGO_NEW = "18- Recargo extra nocturno festivo - Ingreso";

    public static final String ABSENTEEISM_INCAPACITY_ARL = "22- Recargo extra nocturno festivo - Ingreso";
    public static final String ABSENTEEISM_INCAPACITY_WITH_SUPPORT = "23- Recargo extra nocturno festivo - Ingreso";
    public static final String ABSENTEEISM_INCAPACITY_WITHOUT_SUPPORT = "24- Recargo extra nocturno festivo - Ingreso";
    public static final String ABSENTEEISM_UNPAID_LEAVE = "25- Recargo extra nocturno festivo - Ingreso";
    public static final String ABSENTEEISM_PAID_LEAVE = "26- Recargo extra nocturno festivo - Ingreso";
    public static final String ABSENTEEISM = "27- Recargo extra nocturno festivo - Ingreso";
    public static final String ABSENTEEISM_EPS_COLLABORATOR = "28- Recargo extra nocturno festivo - Ingreso";
    public static final String ABSENTEEISM_QUIT = "29- Recargo extra nocturno festivo - Ingreso";
    public static final String ABSENTEEISM_DAY_OFF = "30- Recargo extra nocturno festivo - Ingreso";

    public static final String EMPTY_NAME_VALUE_MESSAGE_ERROR = "El campo nombre no puede estar vacio.";
    public static final String DOCUMENT_ID_VALUE_MESSAGE_ERROR = "El valor '%s' no es válido como numero de identificacion del empleado.";
    public static final String LAST_VALUE_FIRST_FORTNIGHT_MESSAGE_ERROR = "El ultimo dia la quincena debe ser 15 pero después de esa columana se encontro el valor: %s";
    public static final String LAST_VALUE_SECOND_FORTNIGHT_MESSAGE_ERROR = "El ultimo dia de %s es %s pero después de esa columana se encontro el valor: %s";
    public static final String NOT_CORRESPONDING_QUANTITY_OF_DAYS_FOR_FORTNIGHT_MESSAGE_ERROR = "La fila %s no contiene la candidad de dias correspondiente.";
    public static final String INVALID_VALUE_MESSAGE_ERROR = "'%s' no, es un valor válido.";


}
