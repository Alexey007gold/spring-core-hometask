package ua.epam.spring.hometask.dao.interf;

import ua.epam.spring.hometask.domain.Auditorium;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by Oleksii_Kovetskyi on 4/23/2018.
 */
public interface AuditoriumDAO {
    @Nonnull
    Set<Auditorium> getAll();

    @Nullable
    Auditorium getByName(@Nonnull String name);
}
