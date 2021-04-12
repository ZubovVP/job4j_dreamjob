package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.forTest.PsqlStoreForTest;
import ru.job4j.dream.store.PsqlStore;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 12.04.2021.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest {

    @Test
    public void whenAddPostThenStoreIt() throws ServletException, IOException {
        PsqlStoreForTest store = new PsqlStoreForTest();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("TestName");
        when(req.getParameter("description")).thenReturn("TestDecr");
        when(req.getParameter("created")).thenReturn("1992-02-22");
        new PostServlet().doPost(req, resp);
        assertEquals(1, store.findAll().size());
        assertThat((store.findAll().iterator().next().getName()), is("TestName"));
    }
}