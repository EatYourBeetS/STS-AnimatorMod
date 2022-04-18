package eatyourbeets.cards.unnamed.basic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Strike extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Strike.class)
            .SetAttack(1, CardRarity.BASIC);

    public Strike()
    {
        super(DATA);

        Initialize(5, 0, 1);
        SetUpgrade(2, 0, 1);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT)
        .SetSoundPitch(0.8f, 0.85f).SetVFXColor(Color.RED);

        if (!GameUtilities.HasArtifact(m))
        {
            GameActions.Bottom.StackAmplification(p, m, magicNumber)
            .ShowEffect(false, true);
        }
    }
}