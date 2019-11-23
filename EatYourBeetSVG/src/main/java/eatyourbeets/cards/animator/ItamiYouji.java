package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class ItamiYouji extends AnimatorCard
{
    public static final String ID = Register(ItamiYouji.class.getSimpleName(), EYBCardBadge.Synergy);

    public ItamiYouji()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(2,0,4, 1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DrawCard(p, magicNumber, this::OnDraw, m);

        if (HasActiveSynergy())
        {
            int supportDamage = secondaryValue * GameUtilities.GetCurrentEnemies(true).size();
            if (supportDamage > 0)
            {
                GameActionsHelper.ApplyPower(p, p, new SupportDamagePower(p, supportDamage), supportDamage);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }

    private void OnDraw(Object state, ArrayList<AbstractCard> cards)
    {
        AbstractMonster m = JavaUtilities.SafeCast(state, AbstractMonster.class);
        if (m != null && cards != null && cards.size() > 0)
        {
            for (AbstractCard c : cards)
            {
                GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
                GameActionsHelper.DamageTargetPiercing(AbstractDungeon.player, m, damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
            }
        }
    }
}