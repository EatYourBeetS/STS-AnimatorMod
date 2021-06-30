package eatyourbeets.cards.animator.auras;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Aura3 extends Aura
{
    public static final EYBCardData DATA = RegisterAura(Aura3.class);

    public Aura3()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(p), 1)
        .IgnoreArtifact(true);
    }
}