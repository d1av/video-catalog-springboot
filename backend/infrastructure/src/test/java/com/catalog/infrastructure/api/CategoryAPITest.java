package com.catalog.infrastructure.api;

import com.catalog.ControllerTest;
import com.catalog.application.category.create.CreateCategoryOutput;
import com.catalog.application.category.create.CreateCategoryUseCase;
import com.catalog.application.category.delete.DeleteCategoryUseCase;
import com.catalog.application.category.retrieve.get.CategoryOutput;
import com.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.catalog.application.category.update.UpdateCategoryOutput;
import com.catalog.application.category.update.UpdateCategoryUseCase;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.exceptions.DomainException;
import com.catalog.domain.exceptions.NotFoundException;
import com.catalog.domain.validation.Error;
import com.catalog.domain.validation.handler.Notification;
import com.catalog.infrastructure.category.models.CreateCategoryApiInput;
import com.catalog.infrastructure.category.models.UpdateCategoryApiInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;
    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;
    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;
    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from(CategoryID.from("123"))));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpectAll(status().isCreated())
                .andExpect(header().string("Location", "/categories/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));


        verify(createCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())

                ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(status().isUnprocessableEntity());
        // then
        response.andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));


        verify(createCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())

                ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        // when

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());
        // then
        response.andExpectAll(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));


        verify(createCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())

                ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(aCategory));

        // when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = this.mvc.perform(request)
                .andDo(print());
        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));


    }

    @Test
    public void givenInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");
        // when
        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = this.mvc.perform(request)
                .andDo(print());
        //then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

    }


    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);


        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        final var aCommand =
                new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);
        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());
        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));


    }

    @Test
    public void givenAInvalidId_whenCallsUpdateCategory_thenShouldReturnNotFoundException() throws Exception {
        // given
        final var expectedId = "123-NOT-FOUND";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID 123-NOT-FOUND was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var aCommand =
                new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);
        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());
        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));


    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final var expectedId = "123-NOT-FOUND";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var aCommand =
                new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);
        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());
        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));


    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk() throws Exception {
        // given
        final var expectedId = "123";


        doNothing()
                .when(deleteCategoryUseCase).execute(any());

        // when
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = this.mvc.perform(request)
                .andDo(print());
        // then
        response.andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));


    }

}
