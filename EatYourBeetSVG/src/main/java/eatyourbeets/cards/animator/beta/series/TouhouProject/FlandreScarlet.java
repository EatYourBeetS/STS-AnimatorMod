package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.FlandreScarlet_RemiliaScarlet;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class FlandreScarlet extends AnimatorCard
{
    private static final CardEffectChoice choices = new CardEffectChoice();
    public static final EYBCardData DATA = Register(FlandreScarlet.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new FlandreScarlet_RemiliaScarlet(), false));;

    public FlandreScarlet()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(3, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Dark, 8);

        SetEthereal(true);
    }

    public AbstractAttribute GetSpecialInfo() {
        return HPAttribute.Instance.SetCard(this);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null) {
            CalculateHeal();
        }
        else {
            heal = 0;
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BITE);
        if (CalculateHeal() > 0)
        {
            GameActions.Bottom.RecoverHP(heal);
        }

        GameActions.Last.Callback(() -> {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractCard handCard = JUtils.Random(player.hand.group);

                choices.Initialize(this, true);
                choices.AddEffect(new GenericEffect_FlandreScarlet(0, this));
                if (handCard != null) {
                    choices.AddEffect(new GenericEffect_FlandreScarlet(1, handCard));
                }
                choices.Select(1, null);
            }
        });

        GameActions.Last.Callback(() -> {
            if (CheckAffinity(Affinity.Dark) && info.TryActivateLimited()) {
                GameActions.Bottom.MakeCardInDrawPile(new FlandreScarlet_RemiliaScarlet());
            }
        });
    }

    protected int CalculateHeal()
    {
        return heal = Math.min(damage, GameActionManager.playerHpLastTurn - player.currentHealth);
    }

    protected static class GenericEffect_FlandreScarlet extends GenericEffect
    {
        private final AbstractCard target;
        public GenericEffect_FlandreScarlet(int amount, AbstractCard target)
        {
            this.amount = amount;
            this.target = target;
        }

        @Override
        public String GetText()
        {
            return FlandreScarlet.DATA.Strings.EXTENDED_DESCRIPTION[amount];
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            GameActions.Bottom.Exhaust(target);
        }
    }

}

