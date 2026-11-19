package negocio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import modelo.Usuario;

public class RedSocial {
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<String, Set<String>> amistades = new LinkedHashMap<>();
    private final Map<String, Set<String>> seguimientos = new LinkedHashMap<>();

    public boolean agregarUsuario(String nombre) {
        if (nombre == null || nombre.isBlank() || existeUsuario(nombre)) {
            return false;
        }
        usuarios.put(nombre, new Usuario(nombre));
        amistades.put(nombre, new LinkedHashSet<>());
        seguimientos.put(nombre, new LinkedHashSet<>());
        return true;
    }

    public boolean existeUsuario(String nombre) {
        return usuarios.containsKey(nombre);
    }

    public String agregarAmistad(String a, String b) {
        String error = validarRelacion(a, b, "amigo");
        if (error != null) {
            return error;
        }
        if (sonAmigos(a, b)) {
            return "Rechazado: " + a + " y " + b + " ya son amigos";
        }
        amistades.get(a).add(b);
        amistades.get(b).add(a);
        return "Amistad agregada: " + a + " <-> " + b;
    }

    public String eliminarAmistad(String a, String b) {
        if (!existeUsuario(a) || !existeUsuario(b) || !sonAmigos(a, b)) {
            return "Rechazado: no existe la amistad " + a + " <-> " + b;
        }
        amistades.get(a).remove(b);
        amistades.get(b).remove(a);
        return "Amistad eliminada: " + a + " <-> " + b;
    }

    public String seguir(String a, String b) {
        String error = validarRelacion(a, b, "seguidor");
        if (error != null) {
            return error;
        }
        if (sigue(a, b)) {
            return "Rechazado: " + a + " ya sigue a " + b;
        }
        seguimientos.get(a).add(b);
        return "Seguimiento agregado: " + a + " -> " + b;
    }

    public String dejarDeSeguir(String a, String b) {
        if (!existeUsuario(a) || !existeUsuario(b) || !sigue(a, b)) {
            return "Rechazado: no existe el seguimiento " + a + " -> " + b;
        }
        seguimientos.get(a).remove(b);
        return "Seguimiento eliminado: " + a + " -> " + b;
    }

    public List<String> eliminarUsuario(String nombre) {
        List<String> eliminadas = new ArrayList<>();
        if (!existeUsuario(nombre)) {
            eliminadas.add("Rechazado: el usuario " + nombre + " no existe");
            return eliminadas;
        }
        for (String amigo : amistades.get(nombre)) {
            amistades.get(amigo).remove(nombre);
            eliminadas.add("Amistad " + amigo + " <-> " + nombre + " (eliminada)");
        }
        for (String seguido : seguimientos.get(nombre)) {
            eliminadas.add("Seguimiento " + nombre + " -> " + seguido + " (eliminado)");
        }
        for (Map.Entry<String, Set<String>> entrada : seguimientos.entrySet()) {
            if (!entrada.getKey().equals(nombre) && entrada.getValue().remove(nombre)) {
                eliminadas.add("Seguimiento " + entrada.getKey() + " -> " + nombre + " (eliminado)");
            }
        }
        amistades.remove(nombre);
        seguimientos.remove(nombre);
        usuarios.remove(nombre);
        eliminadas.add("Usuario " + nombre + " eliminado");
        return eliminadas;
    }

    public boolean sonAmigos(String a, String b) {
        return amistades.containsKey(a) && amistades.get(a).contains(b);
    }

    public boolean sigue(String a, String b) {
        return seguimientos.containsKey(a) && seguimientos.get(a).contains(b);
    }

    public boolean apareceEnAlgunaConexion(String nombre) {
        for (Set<String> amigos : amistades.values()) {
            if (amigos.contains(nombre)) return true;
        }
        for (Set<String> seguidos : seguimientos.values()) {
            if (seguidos.contains(nombre)) return true;
        }
        return amistades.containsKey(nombre) || seguimientos.containsKey(nombre);
    }

    public List<String> seguidoresDe(String nombre) {
        List<String> seguidores = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entrada : seguimientos.entrySet()) {
            if (entrada.getValue().contains(nombre)) {
                seguidores.add(entrada.getKey());
            }
        }
        return seguidores;
    }

    public String mostrarRed() {
        if (usuarios.isEmpty()) {
            return "La red social esta vacia";
        }
        StringBuilder sb = new StringBuilder();
        for (String nombre : usuarios.keySet()) {
            sb.append(String.format("%-7s| amigos: %-16s| sigue a: %-10s| seguidores: %s%n",
                    nombre,
                    formatear(amistades.get(nombre)),
                    formatear(seguimientos.get(nombre)),
                    formatear(seguidoresDe(nombre))));
        }
        return sb.toString();
    }

    public String listarUsuarios() {
        return String.join(", ", usuarios.keySet());
    }

    public int cantidadUsuarios() {
        return usuarios.size();
    }

    private String validarRelacion(String a, String b, String tipo) {
        if (!existeUsuario(a)) {
            return "Rechazado: el usuario " + a + " no existe";
        }
        if (!existeUsuario(b)) {
            return "Rechazado: el usuario " + b + " no existe";
        }
        if (a.equals(b)) {
            return "Rechazado: " + a + " no puede ser " + tipo + " de si mismo";
        }
        return null;
    }

    private String formatear(Iterable<String> nombres) {
        List<String> lista = new ArrayList<>();
        for (String n : nombres) {
            lista.add(n);
        }
        return lista.isEmpty() ? "-" : String.join(", ", lista);
    }
}
