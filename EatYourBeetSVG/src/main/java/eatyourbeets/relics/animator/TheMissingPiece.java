package eatyourbeets.relics.animator;

public class TheMissingPiece extends AbstractMissingPiece
{
    public static final String ID = CreateFullID(TheMissingPiece.class);
    public static final int REWARD_INTERVAL = 3;

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