package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.event.EventMapper;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> result = new ArrayList<>();
        int page = from > 0 ? from / size : 0;
        Pageable compilationPageable = PageRequest.of(page, size);
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, compilationPageable);
        for (Compilation compilation : compilations) {
            CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
            compilationDto.setEvents(eventMapper.listEventsToListDto(compilation.getEvents()));
            result.add(compilationDto);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Integer compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() -> new NotFoundException("Подборка с ID %s не найдено",compilationId));
        CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
        compilationDto.setEvents(eventMapper.listEventsToListDto(compilation.getEvents()));
        return compilationDto;
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.newCompilationDtoToCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        compilation = compilationRepository.save(compilation);
        CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
        compilationDto.setEvents(eventMapper.listEventsToListDto(compilation.getEvents()));
        return compilationDto;
    }

    @Override
    @Transactional
    public void deleteCompilation(Integer compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() -> new NotFoundException("Подборка с ID %s не найдено",compilationId));
        compilationRepository.deleteById(compilationId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Integer compilationId, UpdateCompilationDto updateCompilationDto) {
        Compilation compilationToUpdate = compilationRepository.findById(compilationId).orElseThrow(() -> new NotFoundException("Подборка с ID %s не найдено",compilationId));
        if (updateCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateCompilationDto.getEvents());
            compilationToUpdate.setEvents(events);
        }
        if (updateCompilationDto.getPinned() != null) {
            compilationToUpdate.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getTitle() != null) {
            compilationToUpdate.setTitle(updateCompilationDto.getTitle());
        }
        compilationToUpdate = compilationRepository.save(compilationToUpdate);
        CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilationToUpdate);
        compilationDto.setEvents(eventMapper.listEventsToListDto(compilationToUpdate.getEvents()));
        return compilationDto;
    }
}
