package org.iota.ict.ixi;

import org.iota.ict.Ict;
import org.iota.ict.utils.properties.EditableProperties;
import org.junit.After;
import org.junit.Before;

import java.util.Set;

public abstract class TestTemplate {

    protected Ict ict1;
    protected Ict ict2;
    private Bridge bridge;

    @Before
    public void setup() {
        EditableProperties properties1 = new EditableProperties().host("localhost").port(1337).minForwardDelay(0).maxForwardDelay(10).guiEnabled(false);
        ict1 = new Ict(properties1.toFinal());
        EditableProperties properties2 = new EditableProperties().host("localhost").port(1338).minForwardDelay(0).maxForwardDelay(10).guiEnabled(false);
        ict2 = new Ict(properties2.toFinal());
        addNeighborToIct(ict1,ict2);
        addNeighborToIct(ict2,ict1);
        bridge = new Bridge(ict1);
        new Thread(() -> bridge.run()).start();
        try { Thread.sleep(500); } catch (Exception e) { ; }
    }

    @After
    public void tearDown() {
        bridge.terminate();
        ict1.terminate();
        ict2.terminate();
        try { Thread.sleep(500); } catch (Exception e) { ; }
    }

    private static void addNeighborToIct(Ict ict, Ict neighbor) {
        EditableProperties properties = ict.getProperties().toEditable();
        Set<String> neighbors = properties.neighbors();
        neighbors.add(neighbor.getAddress().getHostName() + ":" + neighbor.getAddress().getPort());
        properties.neighbors(neighbors);
        ict.updateProperties(properties.toFinal());
    }

}
