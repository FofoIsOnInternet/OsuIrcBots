package multiplayerlobby;

import com.oopsjpeg.osu4j.OsuBeatmap;
import com.oopsjpeg.osu4j.backend.EndpointBeatmaps;
import com.oopsjpeg.osu4j.backend.Osu;
import com.oopsjpeg.osu4j.exception.OsuAPIException;

/**
 *
 * @author fofoisoninternet
 */
public class Map extends OsuBeatmap{
    
    public Map(Osu api, int mapid) throws OsuAPIException{
        super(retrieveInfo(api, mapid));
    }
    
    /**
     * Retrieve the map information using osu's api
     */
    private static OsuBeatmap retrieveInfo(Osu api,int mapid) throws OsuAPIException{
        OsuBeatmap beatmap = api.beatmaps.getAsQuery(new EndpointBeatmaps.ArgumentsBuilder()
				.setBeatmapID(mapid).build())
				.resolve().get(0);
        return beatmap;
    }
    
    public String[] textInfo(){
        String[] lines = new String[3];
        lines[0] = toString() + " [https://beatconnect.io/b/" + getBeatmapSetID() + "/ BeatConnect]";
        lines[1] =  String.format("  %.2f*  |  %d:%02d",getDifficulty(),minutes(),seconds());
        lines[2] = String.format("BPM:%.0f | AR:%.1f | CS:%.1f | OD:%.1f | HP:%.1f",
                                    getBPM(),getApproach(),getSize(),getOverall(),getDrain());
        return lines;
    }
    
    private int minutes(){
        return getTotalLength() / 60;
    }
    private int seconds(){
        return getTotalLength() % 60;
    }
    
    @Override
    public String toString(){
        return getTitle() + " | " + getArtist() + " (" + getID() + ")";
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Map)){
            return false;
        }
        Map m = (Map) o;
        return this.getID() == m.getID();
    }
}
