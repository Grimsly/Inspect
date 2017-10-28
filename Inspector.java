/*Modified from Jordan Kidney's original objectInspector*/

import java.util.*;
import java.lang.reflect.*;


public class Inspector
{
    private int mod;
    private Modifier modCheck = new Modifier();
    
    public Inspector() { }
    
    public void inspect(Object obj, boolean recursive)
    {
        if (checkArray(obj)){
            int length = Array.getLength(obj);
            System.out.println("Field: " + "Component type: " + obj.getClass().getComponentType() + ", Name: " + obj.getClass().getName() + ", Length: " + length);
            for (int i = 0; i < length; i ++) {
                Object arrayElement = Array.get(obj, i);
                System.out.println("[" + i + "] " + arrayElement);
                if (checkArray(arrayElement)){
                    int length2 = Array.getLength(arrayElement);
                    for (int j = 0; j < length2; j ++){
                        Object arrayElement2 = Array.get(arrayElement, j);
                        System.out.println("[" + i + "]" + "[" + j + "] " + arrayElement2);
                    }
                }
                            
            }
        }else{
            Class ObjClass = obj.getClass();

            System.out.println("Class: " + obj + " (recursive = "+recursive+")");
    
            inspectSuper(obj, ObjClass, recursive);
            inspectInterfaces(obj, ObjClass, recursive);
            inspectMethods(ObjClass);
            inspectContructors(ObjClass);
            inspectFields(obj, ObjClass, recursive);
        }
    }
    
    private void inspectSuper(Object obj, Class objClass, boolean recursive){
        if (objClass.getSuperclass() != null){
            Class superClass = objClass.getSuperclass();
            System.out.println(".................");
            System.out.println("Superclass: " + superClass.getName());
            System.out.println("_______________________________________");
            inspectSuper(obj, superClass, recursive);
            inspectInterfaces(obj, superClass, recursive);
            inspectMethods(superClass);
            inspectContructors(superClass);
            inspectFields(obj, superClass, recursive);
            System.out.println(".................");
        }
    }
    
    private void inspectInterfaces(Object obj, Class objClass, boolean recursive){
        for (Class c : objClass.getInterfaces()){
            System.out.println(".................");
            System.out.println("Interface: " + c.getName());
            System.out.println("_______________________________________");
            inspectSuper(obj, c, recursive);
            inspectInterfaces(obj, c, recursive);
            inspectMethods(c);
            inspectContructors(c);
            inspectFields(obj, c, recursive);
            System.out.println(".................");
        }
    }
    
    private void inspectMethods(Class objClass){
        for (Method m : objClass.getDeclaredMethods()){
            System.out.println("Method: " + m.getName());
            for (Class e : m.getExceptionTypes()){
                System.out.println("Exception thrown: " + e.getName());
            }
            for (Class p : m.getParameterTypes()){
                System.out.println("Parameter types: " + p.getName());
            }
            
            System.out.println("Return type: " + m.getReturnType().getName());
            
            mod = m.getModifiers();
            System.out.println("Modifiers: " + modCheck.toString(mod) + "\n");
        }
    }
    
    private void inspectContructors(Class objClass){
        for (Constructor c : objClass.getDeclaredConstructors()){
            System.out.println("Constructor: " + c.getName());
            for (Class p : c.getParameterTypes()){
                System.out.println("Parameter type: " + p.getName());
            }
            mod = c.getModifiers();
            System.out.println("Modifiers: " + modCheck.toString(mod)+ "\n");
        }
    }
    
    private void inspectFields(Object obj,Class ObjClass, boolean recursive){
	
        if(ObjClass.getDeclaredFields().length >= 1)
        {
            for(Field f : ObjClass.getDeclaredFields()){
		
                f.setAccessible(true);
                
                try{
		
                    if((!f.getType().isPrimitive()) && recursive && checkArray(f.get(obj))){
                        int length = Array.getLength(f.get(obj));
                        System.out.println("Field: " + "Component type: " + f.get(obj).getClass().getComponentType() + ", Name: " + f.getName() + ", Length: " + length);
                        for (int i = 0; i < length; i ++) {
                            Object arrayElement = Array.get(f.get(obj), i);
                            System.out.println("[" + i + "] " + arrayElement);
                            if (checkArray(arrayElement)){
                                int length2 = Array.getLength(arrayElement);
                                for (int j = 0; j < length2; j ++){
                                    Object arrayElement2 = Array.get(arrayElement, j);
                                    System.out.println("[" + i + "]" + "[" + j + "] " + arrayElement);
                                }
                            }
                            
                        }
                        
                    }else if((!f.getType().isPrimitive()) && (recursive == false) && checkArray(f.get(obj))){
                        int length = Array.getLength(f.get(obj));
                        System.out.println("Field: " + "Component type: " + f.get(obj).getClass().getComponentType() + ", Name: " + f.getName() + ", Length: " + length);
                        for (int i = 0; i < length; i ++) {
                            Object arrayElement = Array.get(f.get(obj), i);
                            System.out.println("[" + i + "] " + arrayElement);
                            if (checkArray(arrayElement)){
                                int length2 = Array.getLength(arrayElement);
                                for (int j = 0; j < length2; j ++){
                                    Object arrayElement2 = Array.get(arrayElement, j);
                                    System.out.println("[" + i + "]" + "[" + j + "] " + arrayElement);
                                }
                            }
                            
                        }
                        
                    }else if((!f.getType().isPrimitive()) && recursive){

                        System.out.println("Field: " + modCheck.toString(f.getModifiers()) + " " + f.getType().getName() + " " + f.getName() + " = " + f.get(obj));
                        System.out.println("Inspecting Field: " + f.getName() );
                        System.out.println("******************");
                        if (f.get(obj) != null){
                            inspect( f.get(obj) , recursive);
                        }else{
                            System.out.println("Null");
                        }
                        System.out.println("******************");
                    }
                    else if((!f.getType().isPrimitive()) && (recursive == false)){
                        checkArray(f.get(obj));
                        System.out.println("Field: " + modCheck.toString(f.getModifiers()) + " " + f.getType().getName() + " " + f.getName() + " = " + f.get(obj) + "@" + Integer.toHexString(f.hashCode()));
                    }
                    else{
                        System.out.println("Field: " + modCheck.toString(f.getModifiers()) + " " + f.getType().getName() + " " + f.getName() + " = " + f.get(obj));
                
                    }
                }catch(Exception exp) { exp.printStackTrace(); }
            }

        }
    }
    
    private boolean checkArray(Object obj){
        try{
            if (obj.getClass().isArray()){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }
}
