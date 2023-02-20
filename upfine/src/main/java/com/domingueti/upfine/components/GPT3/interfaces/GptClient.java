package com.domingueti.upfine.components.GPT3.interfaces;

import java.io.IOException;

public interface GptClient {

    String summarizeText(String text, int maxSummaryLength) throws IOException;

}
