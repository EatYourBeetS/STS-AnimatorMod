package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.powers.SupportDamagePower;

public class Guren extends AnimatorCard
{
    public static final String ID = CreateFullID(Guren.class.getSimpleName());

    public Guren()
    {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 10);

        //AddExtendedDescription();

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);

        AbstractCard attack = GetRandomAttack(p);
        if (attack != null)
        {
            int damage;
            attack.applyPowers();
            //attack.calculateCardDamage(null);
            damage = attack.damage;

            if (damage > 0)
            {
                ShowCardBrieflyEffect effect = new ShowCardBrieflyEffect(attack, Settings.WIDTH / 3f, Settings.HEIGHT / 2f);

                AbstractDungeon.effectsQueue.add(effect);
                GameActionsHelper.AddToTop(new ApplyPowerAction(p, p, new SupportDamagePower(p, damage), damage));
                //GameActionsHelper.AddToTop(new WaitAction(effect.duration));
                GameActionsHelper.AddToTop(new ExhaustSpecificCardAction(attack, p.drawPile, true));
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(4);
        }
    }

    private AbstractCard GetRandomAttack(AbstractPlayer p)
    {
        RandomizedList<AbstractCard> attacks = new RandomizedList<>(p.drawPile.getAttacks().group);

        while (attacks.Count() > 0)
        {
            AbstractCard card = attacks.Retrieve(AbstractDungeon.miscRng);
            if (card != null)
            {
                if (card instanceof SupportDamageConvertible)
                {
                    if (((SupportDamageConvertible)card).CanConvertToSupportDamage())
                    {
                        return card;
                    }
                }
                else
                {
                    return card;
                }
            }
        }

        return null;
    }

    public interface SupportDamageConvertible
    {
        boolean CanConvertToSupportDamage();
    }
}