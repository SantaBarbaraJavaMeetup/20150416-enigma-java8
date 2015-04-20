package sctf.io._2015.q1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.*;

import javax.script.ScriptException;

import org.paukov.combinatorics.Factory;

public class BreakEnigma {

    private static final String[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .chars().<String>mapToObj(chr -> Character.valueOf((char) chr).toString())
            .toArray(String[]::new);
    private static final Map<String, String> ROTORS = Arrays.stream(
            new StringBuffer[] { Enigma.rotorI, Enigma.rotorII,
                    Enigma.rotorIII, Enigma.rotorIV, Enigma.rotorV }).collect(
            Collectors.<StringBuffer, String, String> toMap(ref -> {
                if (ref == Enigma.rotorI) {
                    return "1";
                }
                if (ref == Enigma.rotorII) {
                    return "2";
                }
                if (ref == Enigma.rotorIII) {
                    return "3";
                }
                if (ref == Enigma.rotorIV) {
                    return "4";
                }
                if (ref == Enigma.rotorV) {
                    return "5";
                }
                throw new Error();
            }, String::valueOf));/*
                                  * private static final Map<String, String>
                                  * REFLECTORS = Arrays.stream( new
                                  * StringBuffer[] { Enigma.reflector0,
                                  * Enigma.reflectorB, Enigma.reflectorC
                                  * }).collect( Collectors.<StringBuffer,
                                  * String, String> toMap(ref -> { if (ref ==
                                  * Enigma.reflector0) { return "No Reflector";
                                  * } if (ref == Enigma.reflectorB) { return
                                  * "Reflector B"; } if (ref ==
                                  * Enigma.reflectorC) { return "Reflector C"; }
                                  * throw new Error(); }, String::valueOf));
                                  * 
                                  * private static final String
                                  * createPairs(ICombinatoricsVector<String>
                                  * vec) { String s = vec.getValue(0); return
                                  * String.join(" ", s.split(".{2}")); }
                                  */

    public static final <T> Stream<T> stream(Iterable<T> iter) {
        return StreamSupport.stream(iter.spliterator(), false);
    }

    /*
     * private static final List<String> PLUGBOARD_COMBOS = stream(
     * Factory.createPermutationGenerator(Factory
     * .createVector("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""))))
     * .map(BreakEnigma::createPairs).collect(Collectors.toList());
     */

    public static void main(String[] args) throws IOException {
        try {
            String cip = "";
            String target = "NIFLDAAR";
            Consumer<? super String> runCipher = addition -> {
                try {
                    InvokeVars vars = new InvokeVars();
                    /*
                     * vars.printFunc = obj -> { try (BufferedWriter local =
                     * Files .newBufferedWriter(Paths.get(cip + addition +
                     * ".txt"))) { local.write(String.valueOf(obj) + "\n");
                     * local.flush(); } catch (Exception e) {
                     * e.printStackTrace(); } };
                     */
                    System.err.println("FOR ADDITION " + addition);
                    cipherBreak(target, cip + addition, vars);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Error();
                }
            };
            runCipher.accept("THEKEYIS");
            if (1 == 1)
                return;
            stream(
                    Factory.createPermutationWithRepetitionGenerator(
                            Factory.createVector(ALPHABET), target.length()))
                    .map(vec -> String.join("", vec)).parallel()
                    .forEach(runCipher);
        } finally {
        }
    }

    static List<String[]> rotorCombos = stream(
            Factory.createPermutationGenerator(Factory.createVector(ROTORS
                    .keySet())))
            .map(vec -> vec.getVector().stream().skip(2).map(String::valueOf)
                    .toArray(String[]::new)).map(Factory::createVector)
            .distinct()
            .map(vec -> vec.getVector().stream().toArray(String[]::new))
            .collect(Collectors.toList());
    static List<String[]> rotorSetCombos = stream(
            Factory.createPermutationWithRepetitionGenerator(
                    Factory.createVector(ALPHABET), 3)).map(
            vec -> vec.getVector().stream().toArray(String[]::new)).collect(
            Collectors.toList());
    static List<String[]> ringCombos = stream(
            Factory.createPermutationWithRepetitionGenerator(
                    Factory.createVector(ALPHABET), 3)).map(
            vec -> vec.getVector().stream().toArray(String[]::new)).collect(
            Collectors.toList());

    private static class InvokeVars {
        List<String[]> rotorCombos = BreakEnigma.rotorCombos;
        List<String[]> ringCombos = BreakEnigma.ringCombos;
        List<String[]> rotorSetCombos = BreakEnigma.rotorSetCombos;
        Consumer<Object> printFunc = System.err::println;
    }

    public static void cipherBreak(String target, String cip, InvokeVars vars)
            throws NoSuchMethodException, ScriptException {
        vars.ringCombos
                .parallelStream()
                .map(rCombo -> {
                    return vars.rotorSetCombos
                            .stream()
                            .<Stream<String>> map(
                                    setCombo -> {
                                        return vars.rotorCombos
                                                .stream()
                                                .map(combo -> {
                                                    Enigma e = new Enigma(
                                                            combo[0], combo[1],
                                                            combo[2]);
                                                    e.initialSettings(
                                                            setCombo[0],
                                                            setCombo[1],
                                                            setCombo[2],
                                                            rCombo[0],
                                                            rCombo[1],
                                                            rCombo[2]);
                                                    String en = e.encrypt(cip);
                                                    if (en.equals(target)) {
                                                        e.initialSettings(
                                                                setCombo[0],
                                                                setCombo[1],
                                                                setCombo[2],
                                                                rCombo[0],
                                                                rCombo[1],
                                                                rCombo[2]);
                                                        return cip
                                                                + "="
                                                                + target
                                                                + ": "
                                                                + Arrays.toString(combo)
                                                                + " "
                                                                + Arrays.toString(setCombo)
                                                                + " "
                                                                + Arrays.toString(rCombo)
                                                                + "; " + e.encrypt("NIFLDAAROAOWCQHZWWZEBIQQGIL");
                                                    }
                                                    return null;
                                                });
                                    });
                }).flatMap(Function.identity()).flatMap(Function.identity())
                .filter(Objects::nonNull).forEach(vars.printFunc);
    }
}
