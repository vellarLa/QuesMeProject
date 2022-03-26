package QuesMeDemo.services;

import QuesMeDemo.entities.AdminEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import QuesMeDemo.entities.CategoryEntity;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final QuestionService questionService;

    public Optional<CategoryEntity> getById(Integer idCategory) {
        return categoryRepository.findById(idCategory);
    }

    public void delById(Integer idCategory) throws ErrorFieldException, NullFieldException {
        questionService.nullCategory(idCategory);
        categoryRepository.deleteById(idCategory);
    }

    public void updateTitle (Integer id, String title) throws ErrorFieldException, NullFieldException {
        CategoryEntity categoryEntity = getById(id).get();
        categoryEntity.setTitle(title);
        save(categoryEntity);
    }
    public void save(CategoryEntity categoryEntity) throws NullFieldException {
        if (categoryEntity.getTitle() == null)
            throw new NullFieldException();
        categoryRepository.save(categoryEntity);
    }
    public List<CategoryEntity> getAll() {
        return categoryRepository.findAll();
    }

    public void saveAll(List<CategoryEntity> categories) {
        categoryRepository.saveAll(categories);
    }
}
