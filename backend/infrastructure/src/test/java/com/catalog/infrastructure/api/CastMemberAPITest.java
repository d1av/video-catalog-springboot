package com.catalog.infrastructure.api;

import com.catalog.ControllerTest;
import com.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.catalog.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.catalog.application.castmember.update.UpdateCastMemberUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CreateCastMemberUseCase createCastMemberUseCase;
    @MockBean
    private DeleteCastMemberUseCase deleteCastMemberUseCase;
    @MockBean
    private UpdateCastMemberUseCase updateCastMemberUseCase;
    @MockBean
    private GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    @MockBean
    private ListCastMembersUseCase listCastMembersUseCase;


}
