package eatyourbeets.cards.animator;

import basemod.patches.com.megacrit.cardcrawl.characters.AbstractPlayer.MaxHandSizePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.RefreshHandLayoutAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Emonzaemon extends AnimatorCard
{
    public static final String ID = CreateFullID(Emonzaemon.class.getSimpleName());

    public Emonzaemon()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(8,0);

        AddExtendedDescription();

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);

        ArrayList<AbstractCard> cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        int size = cardsPlayed.size();
        if (size >= 3)
        {
            boolean threeInARow = true;
            for (int i = 1; i <= 3; i++)
            {
                if (cardsPlayed.get(size - i).type != CardType.ATTACK)
                {
                    threeInARow = false;
                }
            }

            if (threeInARow)
            {
                EntouJyuu toAdd = new EntouJyuu();
                if (upgraded)
                {
                    toAdd.upgrade();
                }

                GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(toAdd, 1, true, true));
                GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());

                this.purgeOnUse = true;
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}