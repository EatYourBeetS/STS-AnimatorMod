package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.actions.special.SelectCreature;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class Caster extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Caster.class).SetSkill(1, CardRarity.UNCOMMON);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Caster()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetEthereal(true);
        SetSynergy(Synergies.Fate);
        SetSpellcaster();
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            GameUtilities.GetIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (HasSynergy())
        {
            target = CardTarget.SELF_AND_ENEMY;
        }
        else
        {
            target = CardTarget.SELF;
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        ChooseAction(null);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Dark());

        if (HasSynergy())
        {
            ChooseAction(m);
        }
    }

    public void ChooseAction(AbstractMonster m)
    {
        if (choices.TryInitialize(this))
        {
            String[] text = DATA.Strings.EXTENDED_DESCRIPTION;
            choices.AddEffect(text[0], this::Effect1);
            choices.AddEffect(JUtils.Format(text[1], magicNumber), this::Effect2);
            choices.AddEffect(text[2], this::Effect3);
        }

        choices.Select(secondaryValue, m);
    }

    private void Effect1(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.EvokeOrb(1);
    }

    private void Effect2(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (!GameUtilities.IsValidTarget(m))
        {
            ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
            if (enemies.isEmpty())
            {
                return;
            }
            else if (enemies.size() > 1)
            {
                GameActions.Bottom.SelectCreature(SelectCreature.Targeting.Enemy, card.name)
                .SetMessage(card.rawDescription)
                .SetOnHovering(c ->
                {
                    if (c instanceof AbstractMonster)
                    {
                        GameUtilities.GetIntent((AbstractMonster)c).AddStrength(-magicNumber);
                    }
                })
                .AddCallback(m1 -> GameActions.Top.ReduceStrength(m1, magicNumber, true));
                return;
            }

            m = enemies.get(0);
        }

        GameActions.Bottom.ReduceStrength(m, magicNumber, true);
    }

    private void Effect3(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainForce(1);
        GameActions.Bottom.GainIntellect(1);
    }
}