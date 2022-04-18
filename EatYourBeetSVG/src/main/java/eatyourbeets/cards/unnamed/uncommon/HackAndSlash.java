package eatyourbeets.cards.unnamed.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class HackAndSlash extends UnnamedCard
{
    public static final EYBCardData DATA = Register(HackAndSlash.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Piercing, EYBCardTarget.Random)
            .ObtainableAsReward((data, deck) -> deck.size() >= (10 + (12 * data.GetTotalCopies(deck))));

    public HackAndSlash()
    {
        super(DATA);

        Initialize(5, 0, 1, 2);
        SetUpgrade(1, 0, 1);

        SetRecast(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY).SetVFXColor(Color.PURPLE);
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.ReshuffleFromHand(name, 1, false)
        .SetOptions(false, false, false);

        if (!hasTag(RECAST))
        {
            GameActions.Bottom.GainTemporaryStats((magicNumber + (IsSolo() ? secondaryValue : 0)), 0, 0);
        }
    }
}