package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Enchantment3 extends Enchantment
{
    public static final int INDEX = 3;
    public static final EYBCardData DATA = RegisterAura(Enchantment3.class);

    public Enchantment3()
    {
        super(DATA, INDEX);

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