package eatyourbeets.cards.animator.beta.Rewrite;

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
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ShizuruNakatsu extends AnimatorCard {
    public static final EYBCardData DATA = Register(ShizuruNakatsu.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);
    private boolean canAttack;

    public ShizuruNakatsu() {
        super(DATA);

        Initialize(8, 5, 2,1);
        SetUpgrade(0,3,0);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (canAttack)
        {
            return super.GetDamageInfo().AddMultiplier(GetNumberOfSkills(player.discardPile));
        }

        return null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.DiscardFromHand(name, magicNumber, true)
        .ShowEffect(true, true)
        .SetFilter(c -> c.type == CardType.SKILL)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
        });

        AgilityPower agility = GameUtilities.GetPower(player, AgilityPower.class);
        if (agility != null && agility.GetCurrentLevel() > 2)
        {
            canAttack = true;
        }

        if (canAttack)
        {
            GameActions.Bottom.SFX("ATTACK_HEAVY");
            GameActions.Bottom.VFX(new DieDieDieEffect());

            int[] damageMatrix = DamageInfo.createDamageMatrix(damage, true);

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