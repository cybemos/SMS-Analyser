package com.cybemos.analyser.data.parser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cybemos.analyser.R;
import com.cybemos.analyser.data.Util;
import com.cybemos.analyser.data.exceptions.NumberAccessException;
import com.cybemos.analyser.data.statistics.JSONStatistics;
import com.cybemos.analyser.data.statistics.Statistics;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.cybemos.analyser.data.Util.getMyNumber;

/**
 * JSON Parser
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class JSONParser implements Parser {

    private static final String EXTENSION = "json";

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<Extension> getExtensions() {
        List<Extension> list = new ArrayList<>();
        list.add(new Extension(EXTENSION, R.string.format_json));
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(@Nullable String extension) {
        return EXTENSION.equals(extension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(@NonNull Statistics statistics, @NonNull File save) {
        boolean res = false;
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(save, new JSONStatistics(statistics));
            res = true;
        } catch (NumberAccessException | IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Statistics load(@NonNull File file) {
        ObjectMapper mapper = new ObjectMapper();
        Statistics statistics = null;
        try {
            JSONStatistics json = mapper.readValue(file, JSONStatistics.class);
            statistics = json.toStatistics();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statistics;
    }
}
