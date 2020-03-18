package eatyourbeets.relics.animator;

public class TheMissingPiece extends AbstractMissingPiece
{
    public static final String ID = CreateFullID(TheMissingPiece.class);

    private static final int REWARD_INTERVAL = 4;

    public TheMissingPiece()
    {
        super(ID, RelicTier.STARTER, LandingSound.FLAT);
    }

    @Override
    protected int GetRewardInterval()
    {
        return REWARD_INTERVAL;
    }
}