package genealogy;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Output the class hierarchy or ancestry of a class.
 *
 * @author hom
 */
public class Genealogy {
    javax.swing.JButton btn;
    int c = new ArrayList<Class>().stream().mapToInt( p -> 1 ).sum();
void Genealogy(){
    this.btn= new JButton();
}
    /**
     * @param args the command line arguments.
     */
    public static void main( String[] args ) {
        if ( args.length == 0 ) {
            System.out.println( "genealogy.Genealogy classname"
                    + " [[classname]...]\n"
                    + "as in genealogy.Genealogy java.lang.String"
            );
        }

        Genealogy gen = new Genealogy();
        for ( String arg : args ) {
            String sb = gen.getAncestry( arg );
            System.out.format( "class hierarchy of [%s]%n", arg );
            System.out.println( sb );
        }
    }

    /**
     * Get the ancestry of the class or interface with the given name.
     *
     * @param typeName to use
     * @return a string containing the type hierarchy of the type
     */
    public String getAncestry( String typeName ) {
        List<Class<?>> classes = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        int count = 0;
        Predicate<Field> nonStatic = ( Field f ) -> !Modifier.isStatic( f.getModifiers() );

        try {
            Class<?> classesNames = Class.forName(typeName);

            while (classesNames != null) {
                classes.addAll(Arrays.asList(classesNames.getInterfaces()));
                classes.add(classesNames);
                classesNames = classesNames.getSuperclass();

                List<Field> list = new ArrayList<>();
                if (classesNames!= null) {
                    Collections.addAll(list, classesNames.getDeclaredFields());
                }
                int j;
                for(j = 0; j < list.size(); j++) {
                    if(nonStatic.test(list.get(j))){
                        fields.add(list.get(j));
                    }
                }
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Genealogy.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = classes.size() - 1; i >= 0; i--) {
            message.append("  ".repeat(count)).append(classes.get(i).getName()).append("\n");
            count++;
        }

        for(int i = 0; i < fields.size(); i++){
            message.append(fields.get(i).getName()).append("\n");
        }

        return message.toString();
    }
}
