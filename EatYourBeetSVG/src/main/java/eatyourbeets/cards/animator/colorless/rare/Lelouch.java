package eatyourbeets.cards.animator.colorless.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.animator.GeassPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Lelouch extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lelouch.class)
            .SetSkill(3, CardRarity.RARE, EYBCardTarget.ALL)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.CodeGeass);
    public static final PowerHelper GEASS = new PowerHelper(GeassPower.POWER_ID, null, (o, s, a) -> new GeassPower(o));

    public Lelouch()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetCostUpgrade(-1);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);

        SetPurge(true);
        SetEthereal(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            int count = 0;
            for (AbstractCard c : player.hand.group)
            {
                if (c.uuid != uuid)
                {
                    count += 1;
                }
            }

            return count >= magicNumber;
        }

        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ExhaustFromHand(name, magicNumber, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            if (cards.size() >= magicNumber)
            {
                GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
                GameActions.Bottom.SFX("MONSTER_COLLECTOR_DEBUFF");
                GameActions.Bottom.ApplyPower(TargetHelper.Enemies(), GEASS);
            }
        });
    }
}