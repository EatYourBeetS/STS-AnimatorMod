package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.misc.VestaElixirEffects.VestaElixirEffect;
import eatyourbeets.misc.VestaElixirEffects.VestaElixirEffect_CompleteFaster;
import eatyourbeets.misc.VestaElixirEffects.VestaElixirEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnStartOfTurnPostDrawSubscriber;

import java.util.ArrayList;

public class Vesta extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = Register(Vesta.class.getSimpleName());

    private int timer;
    private Vesta_Elixir elixir;

    public Vesta()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, -1);

        AddExtendedDescription();

        SetExhaust(true);
        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        VestaElixirEffects.BeginCreateElixir((Vesta) this.makeStatEquivalentCopy());
    }

    public void ResearchElixir(Vesta_Elixir elixir)
    {
        this.elixir = elixir;
        this.timer = magicNumber;

        ArrayList<VestaElixirEffect> effects = new ArrayList<>();
        for (VestaElixirEffect effect : elixir.effects)
        {
            if (effect instanceof VestaElixirEffect_CompleteFaster)
            {
                this.timer -= 1;
            }
            else
            {
                effects.add(effect);
            }
        }

        this.elixir.ApplyEffects(effects);

        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (timer > 0)
        {
            timer -= 1;
        }
        else
        {
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(this));

            GameActions.Bottom.MakeCardInHand(elixir);

            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}