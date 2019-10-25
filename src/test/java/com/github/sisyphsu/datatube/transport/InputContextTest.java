package com.github.sisyphsu.datatube.transport;

import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-10-19 15:07:34
 */
public class InputContextTest {

    @Test
    public void testError() {
        Schema schema = new Schema(true);
        InputContext context = new InputContext(schema);

        try {
            context.findDataByID(Const.ID_PREFIX);
            assert false;
        } catch (Exception e) {
            assert true;
        }

        schema.cxtSymbolAdded.add("HELLO");
        schema.cxtSymbolAdded.add("WORLD");
        context.sync();
        schema.reset();

        schema.cxtSymbolExpired.add(0);
        context.sync();
        schema.reset();

        try {
            context.findDataByID(Const.ID_PREFIX);
            assert false;
        } catch (Exception e) {
            assert true;
        }

        context.findSymbolByID(Const.ID_PREFIX + 1);
        try {
            context.findSymbolByID(Const.ID_PREFIX);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            context.findSymbolByID(99);
            assert false;
        } catch (Exception e) {
            assert true;
        }

        schema.tmpStrings.add("hello");
        context.findStringByID(Const.ID_PREFIX);
        try {
            context.findStringByID(Const.ID_PREFIX + 9);
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    public void testStruct() {
        Schema schema = new Schema(true);
        InputContext context = new InputContext(schema);

        schema.cxtNameAdded.add("id");
        schema.cxtNameAdded.add("name");
        schema.cxtStructAdded.add(new int[]{0, 1});
        context.sync();

        try {
            context.findNameByID(-1);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            context.findNameByID(99);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            context.findNameByID(3);
            assert false;
        } catch (Exception e) {
            assert true;
        }

        try {
            context.findStructByID(-1);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            context.findStructByID(9999);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            context.findStructByID(3);
            assert false;
        } catch (Exception e) {
            assert true;
        }

        schema.reset();
        schema.cxtNameExpired.add(0);
        schema.cxtNameExpired.add(1);
        schema.cxtStructExpired.add(0);
        context.sync();
    }

}
