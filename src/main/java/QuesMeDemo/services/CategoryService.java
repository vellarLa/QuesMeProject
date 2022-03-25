package QuesMeDemo.services;

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

    public Optional<CategoryEntity> getById(Integer idCategory) {
        return categoryRepository.findById(idCategory);
    }

    public void delById(Integer idCategory) {
        categoryRepository.deleteById(idCategory);
    }

    public void save(CategoryEntity categoryEntity) {
        categoryRepository.save(categoryEntity);
    }

    public List<CategoryEntity> getAll() {
        return categoryRepository.findAll();
    }

    public void saveAll(List<CategoryEntity> categories) {
        categoryRepository.saveAll(categories);
    }
}
