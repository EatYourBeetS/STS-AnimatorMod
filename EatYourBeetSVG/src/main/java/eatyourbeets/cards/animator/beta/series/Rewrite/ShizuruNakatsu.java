package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.affinity.AgilityPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ShizuruNakatsu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShizuruNakatsu.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    private boolean canAttack;

    public ShizuruNakatsu()
    {
        super(DATA);

        Initialize(0, 5, 2, 1);
        SetUpgrade(0, 3, 0);
        SetAffinity_Green(2, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        if (!player.stance.ID.equals(AgilityStance.STANCE_ID))
        {
            GameActions.Bottom.DiscardFromHand(name, magicNumber, true)
                    .ShowEffect(true, true)
                    .SetFilter(c -> c.type == CardType.SKILL)
                    .SetOptions(false, false, false)
                    .AddCallback(() -> GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID));
        }

        AgilityPower agility = GameUtilities.GetPower(player, AgilityPower.class);
        if (agility != null && GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID) > 2)
        {
            canAttack = true;
        }

        if (canAttack)
        {
            GameActions.Bottom.SFX("ATTACK_HEAVY");
            GameActions.Bottom.VFX(new DieDieDieEffect());

            int[] damageMatrix = DamageInfo.createDamageMatrix(8, true);
            GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE);
        }
    }

    private int GetNumberOfSkills(CardGroup group)
    {
        int count = 0;

        for (AbstractCard card : group.group)
        {
            if (card.type == CardType.SKILL)
            {
                count++;
            }
        }

        return count;
    }
}