package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class ItamiYouji extends AnimatorCard
{
    public static final String ID = CreateFullID(ItamiYouji.class.getSimpleName());

    public ItamiYouji()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(2,0,3);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        int draw = magicNumber;
        if (HasActiveSynergy())
        {
            draw += 1;
        }

        GameActionsHelper.DrawCard(p, draw, this::OnDraw, m);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(1);
        }
    }

    private void OnDraw(Object state, ArrayList<AbstractCard> cards)
    {
        AbstractMonster m = Utilities.SafeCast(state, AbstractMonster.class);
        if (m != null && cards != null && cards.size() > 0)
        {
            for (AbstractCard c : cards)
            {
                GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
                GameActionsHelper.DamageTarget(AbstractDungeon.player, m, damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
            }
        }
    }
}