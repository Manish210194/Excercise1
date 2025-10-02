package ie.dpd.resource;

import ie.dpd.repository.TaskRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/summaries")
public class TaskSummaries {

    @Inject
    TaskRepository taskRepository;

    public static class SummaryResponse {
        public long id;
        public int completedCount;
        public int pendingCount;

        public SummaryResponse(long id, int completedCount, int pendingCount) {
            this.id = id;
            this.completedCount = completedCount;
            this.pendingCount = pendingCount;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSummary() {
        try {
            int[] counts = taskRepository.getTaskCounts();
            long summaryId = taskRepository.insertTaskSummary(counts[0], counts[1],counts[0]+counts[1]);
            SummaryResponse response = new SummaryResponse(summaryId, counts[0], counts[1]);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
