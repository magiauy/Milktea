package milktea.milktea.Util;

import milktea.milktea.DTO.Unit;

public class CalUnitUtil {
    public static float calUnitConverter(Unit recipeUnit , Unit ingredientUnit , float recipeQuantity, float ingredientQuantity){
        float result = 0;
        if(recipeUnit == ingredientUnit){
            result = ingredientQuantity/recipeQuantity;
        }else{
            if(recipeUnit == Unit.GRAM){
                if(ingredientUnit == Unit.KILOGRAM){
                    result = ingredientQuantity/(recipeQuantity/1000);
                }else if(ingredientUnit == Unit.MILLILITER){
                    result = ingredientQuantity/(recipeQuantity);
                }else if(ingredientUnit == Unit.LITER){
                    result = ingredientQuantity/(recipeQuantity/1000);
                }
            }else if(recipeUnit == Unit.KILOGRAM){
                if(ingredientUnit == Unit.GRAM){
                    result = ingredientQuantity/(recipeQuantity/1000);
                }else if(ingredientUnit == Unit.MILLILITER){
                    result = ingredientQuantity/(recipeQuantity*1000);
                }
            }else if(recipeUnit == Unit.MILLILITER){
                if (ingredientUnit == Unit.LITER) {
                    result = ingredientQuantity / (recipeQuantity / 1000);
                } else if(ingredientUnit == Unit.GRAM){
                    result = ingredientQuantity/recipeQuantity;
                }else if(ingredientUnit == Unit.KILOGRAM){
                    result = ingredientQuantity/(recipeQuantity/1000);
                }
            }
        }
        return result;
    }
    public static float subtractUnitConverter(Unit recipeUnit , Unit ingredientUnit , float recipeQuantity,float ingredientQuantity){
        float result = 0;
        if(recipeUnit == ingredientUnit){
            result = ingredientQuantity - recipeQuantity;
        }else{
            if(recipeUnit == Unit.GRAM){
                if(ingredientUnit == Unit.KILOGRAM){
                    result = ingredientQuantity - (recipeQuantity/1000);
                }else if(ingredientUnit == Unit.MILLILITER){
                    result = ingredientQuantity - recipeQuantity;
                }else if(ingredientUnit == Unit.LITER){
                    result = ingredientQuantity - (recipeQuantity/1000);
                }
            }else if(recipeUnit == Unit.KILOGRAM){
                if(ingredientUnit == Unit.GRAM){
                    result = ingredientQuantity - (recipeQuantity/1000);
                }else if(ingredientUnit == Unit.MILLILITER){
                    result = ingredientQuantity - (recipeQuantity*1000);
                }
            }else if(recipeUnit == Unit.MILLILITER){
                if (ingredientUnit == Unit.LITER) {
                    result = ingredientQuantity - (recipeQuantity / 1000);
                } else if(ingredientUnit == Unit.GRAM){
                    result = ingredientQuantity - (recipeQuantity*1000);
                }else if(ingredientUnit == Unit.KILOGRAM){
                    result = ingredientQuantity - (recipeQuantity/1000);
                }
            }
        }
        return result;
    }

    public static float addUnitConverter(Unit recipeUnit, Unit ingredientUnit, float recipeQuantity, float ingredientQuantity){
        float result = 0;
        if(recipeUnit == ingredientUnit){
            result = ingredientQuantity + recipeQuantity;
        }else{
            if(recipeUnit == Unit.GRAM){
                if(ingredientUnit == Unit.KILOGRAM){
                    result = ingredientQuantity + (recipeQuantity/1000);
                }else if(ingredientUnit == Unit.MILLILITER){
                    result = ingredientQuantity + recipeQuantity;
                }else if(ingredientUnit == Unit.LITER){
                    result = ingredientQuantity + (recipeQuantity/1000);
                }
            }else if(recipeUnit == Unit.KILOGRAM){
                if(ingredientUnit == Unit.GRAM){
                    result = ingredientQuantity + (recipeQuantity/1000);
                }else if(ingredientUnit == Unit.MILLILITER){
                    result = ingredientQuantity + (recipeQuantity*1000);
                }
            }else if(recipeUnit == Unit.MILLILITER){
                if (ingredientUnit == Unit.LITER) {
                    result = ingredientQuantity + (recipeQuantity / 1000);
                } else if(ingredientUnit == Unit.GRAM){
                    result = ingredientQuantity + recipeQuantity;
                }else if(ingredientUnit == Unit.KILOGRAM){
                    result = ingredientQuantity + (recipeQuantity/1000);
                }
            }
        }
        return result;
    }
}
