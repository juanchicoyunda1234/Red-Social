package app;

import negocio.RedSocial;

import java.util.Scanner;

public class Main {
    private static RedSocial red = new RedSocial();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero();
            switch (opcion) {
                case 1 -> agregarUsuario();
                case 2 -> System.out.println(red.agregarAmistad(leer("Usuario 1: "), leer("Usuario 2: ")));
                case 3 -> System.out.println(red.eliminarAmistad(leer("Usuario 1: "), leer("Usuario 2: ")));
                case 4 -> System.out.println(red.seguir(leer("Quien sigue: "), leer("A quien sigue: ")));
                case 5 -> System.out.println(red.dejarDeSeguir(leer("Quien deja de seguir: "), leer("A quien: ")));
                case 6 -> red.eliminarUsuario(leer("Usuario a eliminar: ")).forEach(System.out::println);
                case 7 -> System.out.print(red.mostrarRed());
                case 8 -> datosPrueba();
                case 0 -> System.out.println("Fin del programa");
                default -> System.out.println("Opcion invalida");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== RED SOCIAL (GRAFO DINAMICO) =====");
        System.out.println("1. Agregar usuario");
        System.out.println("2. Agregar amistad (no dirigida)");
        System.out.println("3. Eliminar amistad");
        System.out.println("4. Seguir (dirigido)");
        System.out.println("5. Dejar de seguir");
        System.out.println("6. Eliminar usuario y sus conexiones");
        System.out.println("7. Mostrar red");
        System.out.println("8. Cargar datos de prueba");
        System.out.println("0. Salir");
        System.out.print("Opcion: ");
    }

    private static void agregarUsuario() {
        String nombre = leer("Nombre: ");
        if (red.agregarUsuario(nombre)) {
            System.out.println("Usuario agregado: " + nombre);
        } else {
            System.out.println("Rechazado: nombre vacio o usuario repetido");
        }
    }

    private static void datosPrueba() {
        red = new RedSocial();

        titulo("FASE 1: Creacion de usuarios (vertices)");
        for (String nombre : new String[]{"Ana", "Beto", "Carlos", "Dani"}) {
            red.agregarUsuario(nombre);
        }
        System.out.println("Usuarios: " + red.listarUsuarios());

        titulo("FASE 2: Relaciones iniciales");
        System.out.println("Amistades (no dirigidas, funcionan en ambos sentidos):");
        System.out.println(red.agregarAmistad("Ana", "Beto"));
        System.out.println(red.agregarAmistad("Ana", "Carlos"));
        System.out.println(red.agregarAmistad("Carlos", "Dani"));
        System.out.println("Seguimientos (dirigidos, en un solo sentido):");
        System.out.println(red.seguir("Beto", "Carlos"));
        System.out.println(red.seguir("Dani", "Ana"));
        System.out.println();
        System.out.print(red.mostrarRed());

        titulo("FASE 3: Modificaciones del moderador");
        System.out.println(red.agregarAmistad("Beto", "Dani"));
        System.out.println(red.dejarDeSeguir("Beto", "Carlos"));
        System.out.println(red.seguir("Carlos", "Beto"));
        System.out.println();
        System.out.print(red.mostrarRed());

        titulo("FASE 4: Eliminacion de Carlos y todas sus conexiones");
        red.eliminarUsuario("Carlos").forEach(System.out::println);

        titulo("FASE 5: Red social final");
        System.out.print(red.mostrarRed());

        titulo("VERIFICACION DE REGLAS");
        System.out.println("Regla 1 -> " + red.agregarAmistad("Ana", "Ana"));
        System.out.println("Regla 2 -> Ana amiga de Beto: " + red.sonAmigos("Ana", "Beto")
                + " | Beto amigo de Ana: " + red.sonAmigos("Beto", "Ana"));
        System.out.println("Regla 3 -> Dani sigue a Ana: " + red.sigue("Dani", "Ana")
                + " | Ana sigue a Dani: " + red.sigue("Ana", "Dani"));
        System.out.println("Regla 4 -> Carlos aparece en alguna conexion: " + red.apareceEnAlgunaConexion("Carlos"));
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("--- " + texto + " ---");
    }

    private static String leer(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
