package negocio;

import modelo.Usuario;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public boolean sonAmigos(String a, String b) {
        return amistades.containsKey(a) && amistades.get(a).contains(b);
    }

    public boolean sigue(String a, String b) {
        return seguimientos.containsKey(a) && seguimientos.get(a).contains(b);
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
}
