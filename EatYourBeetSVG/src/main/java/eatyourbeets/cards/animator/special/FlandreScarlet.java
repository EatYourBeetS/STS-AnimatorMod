package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.animator.series.TouhouProject.RemiliaScarlet;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class FlandreScarlet extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FlandreScarlet.class)
            .SetAttack(2, CardRarity.SPECIAL)
            .SetSeries(RemiliaScarlet.DATA.Series)
            .SetMaxCopies(1);

    public FlandreScarlet()
    {
        super(DATA);

        Initialize(9, 0, 1, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Dark(1);
        SetAffinity_Red(2);
        SetAffinity_Star(0, 0, 2);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(GameUtilities.Imitate(this)).Repeat(secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.Claw(enemy.hb, Color.SCARLET, Color.RED).SetScale(1.2f)).duration);

        if (m.hasPower(VulnerablePower.POWER_ID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> GameActions.Bottom.IncreaseScaling(c, Affinity.Star, c.magicNumber));
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle && misc > 0)
        {
            GameActions.Bottom.ApplyVulnerable(player, player, misc);
            final AbstractCard card = GameUtilities.GetMasterDeckInstance(uuid);
            if (card != null)
            {
                card.misc = misc = 0;
            }
        }
    }
}